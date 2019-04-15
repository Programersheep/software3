package Web_Sever;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private ArrayList<Baggage_INFO>info;//�洢����Ĳ���
	private Passenger_Info p_info;
    public Search() {
        super();
        info=new ArrayList<Baggage_INFO>();
        p_info=new Passenger_Info();
        //over_info=new ArrayList<Baggage_INFO>();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String buttonType=new String(request.getParameter("button").getBytes("iso-8859-1"),"utf-8");
		if(buttonType.equals("Refresh")) {
			info.clear();
			request.setAttribute("Info",null);
			request.getRequestDispatcher("/Index2.jsp").forward(request, response);
		}
		else if(buttonType.equals("ADD")) {//�������
			String V_Height=new String(request.getParameter("Height").getBytes("iso-8859-1"), "utf-8");
			String V_Width=new String(request.getParameter("Width").getBytes("iso-8859-1"), "utf-8");
			String V_Long=new String(request.getParameter("Long").getBytes("iso-8859-1"), "utf-8");
			
			String Weight=new String(request.getParameter("Weight").getBytes("iso-8859-1"), "utf-8");
			
			Baggage_INFO b=new Baggage_INFO();
			b.height=Integer.parseInt(V_Height);
			b.length=Integer.parseInt(V_Long);
			b.width=Integer.parseInt(V_Width);
			b.weight=Integer.parseInt(Weight);
			b.compulateORnot=false;
			info.add(b);
			
			String res="";
			for(int i=0;i<info.size();i++) {
				String temp="����:"+String.valueOf(i+1)+" ������"+String.valueOf(info.get(i).weight)+"KG";
				res+=temp;
				temp=String.valueOf(info.get(i).length)+"CM";
				res=res+"����"+temp;
				temp=String.valueOf(info.get(i).width)+"CM";
				res=res+"��"+temp;
				temp=String.valueOf(info.get(i).height)+"CM";
				res=res+"�ߣ�"+temp;
				res+="@";
			}
			request.setAttribute("Info",res);
			request.getRequestDispatcher("/Index2.jsp").forward(request, response);
		}
		else if(buttonType.equals("Search")) {//��ѯ�˷�
			
			p_info.S_Line=new String(request.getParameter("S_Line").getBytes("iso-8859-1"), "utf-8");
			p_info.S_Cabin=new String(request.getParameter("S_Cabin").getBytes("iso-8859-1"), "utf-8");
			p_info.S_Type=new String(request.getParameter("S_Type").getBytes("iso-8859-1"), "utf-8");
			String current_fare=new String(request.getParameter("Money").getBytes("iso-8859-1"),"utf-8");
			p_info.current_fare=Integer.parseInt(current_fare);
			
			String Baggages[]=request.getParameterValues("Baggage1");
			int res=this.Testng(info, p_info, Baggages);
			String resS=String.valueOf(res);
			request.setAttribute("Money",resS);
			request.getRequestDispatcher("/Index2.jsp").forward(request, response);
		}
	}

	public int Compute_Inline(String S_Cabin,String S_Type,int current_fare) {
		float total_price=0.0f;
		int total_weight=0;
		for(int i=0;i<info.size();i++) {
			if(!info.get(i).compulateORnot)continue;
			total_weight+=info.get(i).weight;
		}
		if(S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {
			total_weight=total_weight-20;
		}
		else if(S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
			total_weight=total_weight-10;
		}
		if(S_Cabin.equals("ͷ�Ȳ�")) {
			total_weight=total_weight-40;
		}
		else if(S_Cabin.equals("�����")) {
			total_weight=total_weight-30;
		}
		else if(S_Cabin.equals("���ò�")||S_Cabin.equals("���龭�ò�")) {
			total_weight=total_weight-20;
		}
		else if(S_Type.equals("Ӥ��")||S_Type.equals("��ռ��Ӥ��")){
			total_weight=total_weight-10;
		}
		if(total_weight<=0)return 0;
		else {
			total_price=(float) (total_weight*current_fare*0.015);
			BigDecimal a = new BigDecimal(total_price);
		    a.setScale(0,BigDecimal.ROUND_HALF_UP);
		    System.out.println(a.intValue());
		    return a.intValue();
		}
	}
	public int Compute_Outline_1(String S_Cabin,String S_Type) {
		int total_price=0;
		int total_count=info.size();
		if(S_Cabin.equals("ͷ�Ȳ�")||S_Cabin.equals("�����")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("�����")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//ͷ�Ȳ�\�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("���龭�ò�")||S_Cabin.equals("���ò�")) {
			int flag1=2;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
				
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>23&&info.get(i).weight<=32) {
							total_price+=1000;
						}
						else if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		return total_price;
	}
	public int Compute_Outline_2(String S_Cabin,String S_Type) {
		int total_price=0;
		int total_count=info.size();
		
		int flag1=3;
		int flag2=1;
		int over_count=total_count-4;
		if(S_Cabin.equals("�����")) {
			flag1--;
			over_count++;
		}
		if(S_Cabin.equals("���龭�ò�")||S_Cabin.equals("���ò�")) {
			flag1=flag1-2;
			over_count=over_count+2;
		}
		if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ����ռ��Ӥ��")) {
			over_count++;
			flag2=0;
		}
		if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
			flag1++;
			flag2--;
		}
		if(over_count==1) {
			total_price=total_price+450;
		}
		else if(over_count>1) {
			total_price=total_price+(over_count-1)*1300+450;
		}
		for(int i=0;i<info.size();i++) {
			if(!info.get(i).compulateORnot)continue;
			if(info.get(i).weight<=20&&flag2==1&&
					S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
				if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
					total_price+=1000;
				}
				flag2--;
			}
			else if(info.get(i).weight<=10&&flag2==1&&
					S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
				if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
					total_price+=1000;
				}
				flag2--;
			}
			else if(info.get(i).weight<=32&&flag1>0) {//
				if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
					total_price+=1000;
				}
				flag1--;
			}
			else{//�жϳ����볬�ߴ�
				if(info.get(i).weight>32) {
					total_price+=3000;
				}
				if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
					total_price+=1000;
				}
			}
		}
		
		return total_price;
	}
	public int Compute_Outline_3(String S_Cabin,String S_Type) {
		int total_price=0;
		int total_count=info.size();
		if(S_Cabin.equals("ͷ�Ȳ�")||S_Cabin.equals("�����")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("�����")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//ͷ�Ȳ�\�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("���龭�ò�")||S_Cabin.equals("���ò�")) {
			int flag1=2;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
				
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>23&&info.get(i).weight<=32) {
							total_price+=2000;
						}
						else if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		return total_price;
	}
	public int Compute_Outline_4(String S_Cabin,String S_Type) {
		int total_price=0;
		int total_count=info.size();
		
		if(S_Cabin.equals("ͷ�Ȳ�")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Type.equals("��ѧ�������񡢺�Ա")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(over_count==1) {
				total_price+=450;
			}
			else if(over_count>1) {
				total_price=total_price+(over_count-1)*1300+450;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
				if(info.get(i).weight<=20&&flag2==1&&
						S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=10&&flag2==1&&
						S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=32&&flag1>0) {//
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
						total_price+=1000;
					}
					flag1--;
				}
				else{//�жϳ����볬�ߴ�
					if(info.get(i).weight>32) {
						total_price+=3000;
					}
					if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
						total_price+=1000;
					}
				}
			}
		}
		else {//����ա����龭�òա����ò�
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Type.equals("��ѧ�������񡢺�Ա")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(S_Cabin.equals("���龭�ò�")) {
				flag1--;
				over_count++;
			}
			if(S_Cabin.equals("���ò�")) {
				flag1=flag1-2;
				over_count=over_count+2;
			}
			if(over_count==1) {
				total_price+=450;
			}
			else if(over_count>1) {
				total_price=total_price+(over_count-1)*1300+450;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
				if(info.get(i).weight<=20&&flag2==1&&
						S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=10&&flag2==1&&
						S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=23&&flag1>0) {//
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
						total_price+=1000;
					}
					flag1--;
				}
				else{//�жϳ����볬�ߴ�
					if(info.get(i).weight>32) {
						total_price+=3000;
					}
					else if(info.get(i).weight>=23&&info.get(i).weight<32) {
						total_price+=1000;
					}
					if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
						total_price+=1000;
					}
				}
			}
		}
		return total_price;
	}
	public int Compute_Outline_5(String S_Cabin,String S_Type) {
		int total_price=0;
		int total_count=info.size();
		if(S_Cabin.equals("ͷ�Ȳ�")||S_Cabin.equals("�����")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("�����")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//ͷ�Ȳ�\�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						else if(info.get(i).weight<=32&&info.get(i).weight>23) {
							total_price+=1000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("���龭�ò�")||S_Cabin.equals("���ò�")) {
			int flag1=2;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
				
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>23&&info.get(i).weight<=32) {
							total_price+=1000;
						}
						else if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		return total_price;
	}
	public int Compute_Outline_6(String S_Cabin,String S_Type) {
		int total_price=0;
		int total_count=info.size();
		if(S_Cabin.equals("ͷ�Ȳ�")||S_Cabin.equals("�����")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("�����")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//ͷ�Ȳ�\�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("���龭�ò�")||S_Cabin.equals("���ò�")) {
			int flag1=1;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("��ѧ�������񡢺�Ա ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("��ͨ�ÿ�")||S_Type.equals("��ͯ��ռ��Ӥ��")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("�Ϻ�����𿨻�Ա��������˳�����Ӣ")) {//�������������
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("�Ϻ�����������Ա��������˾�Ӣ")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���������ߴ�
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0&&
							S_Cabin.equals("���ò�")) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else if(info.get(i).weight<=32&&flag1>0&&
							S_Cabin.equals("���龭�ò�")) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//���ߴ�
							total_price+=1000;
						}
						flag1--;
					}
					else{//�жϳ����볬�ߴ�
						if(info.get(i).weight>23&&info.get(i).weight<=32) {
							total_price+=1000;
						}
						else if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		return total_price;
	}

	@Test(dataProvider="user")
	public void TestIN(String b,String p,int resS) {
		ArrayList<Baggage_INFO>aOFb=new ArrayList<Baggage_INFO>();
		String []binfo=b.split("@");
		//System.out.println(binfo.length);
		for(int i=0;i<binfo.length;i++) {
			String []temp=binfo[i].split("\\+");
			//System.out.println(temp[0]+" "+temp[1]+" "+temp[2]+" "+temp[3]);
			Baggage_INFO bi=new Baggage_INFO(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),true);
			aOFb.add(bi);
		}
		String []pinfo=p.split("\\+");
		Passenger_Info pi=new Passenger_Info(pinfo[0],pinfo[1],pinfo[2],Integer.parseInt(pinfo[3]));
		String Bagg[] = new String[aOFb.size()];
		for(int i=0;i<aOFb.size();i++) {
			Bagg[i]=String.valueOf(i);
		}
		int res=Testng(aOFb,pi,Bagg);
		//if(res==resS)System.out.println("True"+res+"="+resS);
		//else System.out.println("Flase"+res+"!="+resS);
		Assert.assertEquals(res,resS);
	}
	 @DataProvider(name="user")
	    public Object[][] Users(){	   
	        return new Object[][]{
	        	{"50+50+50+42","��ͨ�ÿ�+���ò�+���ʺ���һ+10000",3000},
	        	{"50+50+50+20","��ͨ�ÿ�+���ò�+���ʺ���һ+10000",0},
	        	{"0+0+0+0","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",-2},
	        	{"50+50+10+5","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",0},
	        	{"50+50+10+11","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",-2},
	        	{"50+50+20+5","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",-2},
	        	{"50+50+20+11","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",-2},
	        	{"50+50+10+5@50+50+10+5","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",-2},
	        	{"50+50+20+5@50+50+20+11","��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",-2},
	        	{"50+50+10+5","��ռ��Ӥ��+ͷ�Ȳ�+���ʺ���һ+10000",0},
	        	{"50+50+10+11","��ռ��Ӥ��+���ò�+���ʺ���һ+10000",-2},
	        	{"50+50+10+5@50+50+10+5","��ռ��Ӥ��+���ò�+���ʺ���һ+10000",-2},
	        	
	        	{"50+50+10+0","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"0+0+0+20","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"50+-20+10+20","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"-10+50+10+20","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"50+50+-10+20","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"50+50+10+-10","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"50+50+10+40","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",0},
	        	{"50+50+10+60","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ں���+10000",-1},
	        	{"50+50+10+50","�Ϻ�����𿨻�Ա��������˳�����Ӣ+�����+���ں���+10000",0},
	        	{"50+50+10+40","�Ϻ�����𿨻�Ա��������˳�����Ӣ+���ò�+���ں���+10000",0},
	        	{"50+50+10+50","�Ϻ�����������Ա��������˾�Ӣ+ͷ�Ȳ�+���ں���+10000",0},
	        	{"50+50+10+40","�Ϻ�����������Ա��������˾�Ӣ+�����+���ں���+10000",0},
	        	{"50+50+10+30","�Ϻ�����������Ա��������˾�Ӣ+���ò�+���ں���+10000",0},
	        	{"50+50+10+51","�Ϻ�����������Ա��������˾�Ӣ+ͷ�Ȳ�+���ں���+10000",-1},
	        	
	        	{"50+50+10+41","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+50","��ͨ�ÿ�+ͷ�Ȳ�+���ں���+10000",1500},
	        	{"50+50+10+31","��ͨ�ÿ�+�����+���ں���+10000",150},
	        	{"50+50+10+50","��ͨ�ÿ�+�����+���ں���+10000",3000},
	        	{"50+50+10+21","��ͨ�ÿ�+���ò�+���ں���+10000",150},
	        	{"50+50+10+50","��ͨ�ÿ�+���ò�+���ں���+10000",4500},
	        	{"50+50+10+21","��ͨ�ÿ�+���龭�ò�+���ں���+10000",150},
	        	{"50+50+10+50","��ͨ�ÿ�+���龭�ò�+���ں���+10000",4500},
	        	{"50+50+10+41@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+40@50+50+10+21","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+50@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ں���+10000",1500},
	        	{"50+50+10+50@50+50+10+50","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ں���+10000",6000},
	        	{"50+50+10+40@50+50+10+10@50+50+10+11","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+40@50+50+10+11","�Ϻ�����������Ա��������˾�Ӣ+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+41@50+50+10+10","�Ϻ�����������Ա��������˾�Ӣ+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+40@50+50+10+10@50+50+10+10","�Ϻ�����������Ա��������˾�Ӣ+ͷ�Ȳ�+���ں���+10000",1500},
	        	{"50+50+10+41","��ѧ�������񡢺�Ա+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+40@50+50+10+10","��ѧ�������񡢺�Ա+ͷ�Ȳ�+���ں���+10000",1500},
	        	{"50+50+10+50","��ѧ�������񡢺�Ա+ͷ�Ȳ�+���ں���+10000",1500},
	        	{"50+50+10+41","��ͯ��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",150},
	        	{"50+50+10+50","��ͯ��ռ��Ӥ��+ͷ�Ȳ�+���ں���+10000",1500},
	        	{"50+50+10+30@50+50+10+21","�Ϻ�����𿨻�Ա��������˳�����Ӣ+�����+���ں���+10000",150},
	        	{"50+50+10+31@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+�����+���ں���+10000",150},
	        	{"50+50+10+30@50+50+10+20@50+50+10+10","�Ϻ�����𿨻�Ա��������˳�����Ӣ+�����+���ں���+10000",1500},
	        	{"50+50+10+20@50+50+10+21","�Ϻ�����𿨻�Ա��������˳�����Ӣ+���ò�+���ں���+10000",150},
	        	{"50+50+10+41","�Ϻ�����𿨻�Ա��������˳�����Ӣ+���ò�+���ں���+10000",150},
	        	{"50+50+10+31","�Ϻ�����������Ա��������˾�Ӣ+���ò�+���ں���+10000",150},
	        	
	        	{"50+50+10+32@50+50+10+32@50+50+10+32","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",0},
	        	{"50+50+58+32","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",0},
	        	{"50+50+10+32@50+50+10+32","��ͨ�ÿ�+�����+���ʺ���һ+10000",0},
	        	{"50+50+58+23@50+50+10+23","��ͨ�ÿ�+���ò�+���ʺ���һ+10000",0},
	        	{"50+50+58+32@50+50+10+32@50+50+10+32@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+ͷ�Ȳ�+���ʺ���һ+10000",0},
	        	{"50+50+58+32@50+50+10+32@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+�����+���ʺ���һ+10000",0},
	        	{"50+50+58+23@50+50+10+23@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+���ò�+���ʺ���һ+10000",0},
	        	{"50+50+58+23@50+50+10+23@50+50+10+20","�Ϻ�����𿨻�Ա��������˳�����Ӣ+���龭�ò�+���ʺ���һ+10000",0},
	        	{"50+50+58+32@50+50+10+32@50+50+10+32@50+50+10+10","�Ϻ�����������Ա��������˾�Ӣ+ͷ�Ȳ�+���ʺ���һ+10000",0},
	        	{"50+50+58+23@50+50+10+23@50+50+10+10","�Ϻ�����������Ա��������˾�Ӣ+���ò�+���ʺ���һ+10000",0},
	        	{"50+50+58+32@50+50+10+32@50+50+10+32@50+50+10+32","��ѧ�������񡢺�Ա+ͷ�Ȳ�+���ʺ���һ+10000",0},
	        	{"50+50+58+33@50+50+10+33@50+50+10+33","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",9000},
	        	{"50+50+60+32@50+50+60+32@50+60+50+32","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",3000},
	        	{"50+50+60+33@50+50+60+33@50+60+50+33","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",12000},
	        	{"50+50+50+32@50+50+50+32@50+50+50+32@50+50+50+32","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",1000},
	        	{"50+50+50+32@50+50+50+32@50+50+50+32@50+50+50+32@50+50+50+32","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",3000},
	        	{"50+50+50+33@50+50+60+32@50+50+50+32@50+50+50+32@50+50+50+32","��ͨ�ÿ�+ͷ�Ȳ�+���ʺ���һ+10000",7000}
	        	

	        };
	    }
    public int Testng(ArrayList<Baggage_INFO>bInfo,Passenger_Info pInfo,String[]Baggages) {
    	info=bInfo;
    	if(Baggages==null||Baggages.length==0) {
			return -3;
		}
    	if(pInfo.S_Type.equals("��ռ��Ӥ��")) {
			if(Baggages.length>1) {
				return -2;
			}
			else {
				int j=Integer.parseInt(Baggages[0]);
				if(bInfo.get(j).weight<=10&&bInfo.get(j).weight>0&&(bInfo.get(j).height+info.get(j).length+info.get(j).width)<=115&&
						bInfo.get(j).height>0&&bInfo.get(j).length>0&&bInfo.get(j).length>0) {
					return 0;
				}
				else {
					return -2;
				}
			}
		}
		for(int i=0;i<Baggages.length;i++) {
			int j=Integer.parseInt(Baggages[i]);
			bInfo.get(j).compulateORnot=true;
		}
		int res=0;
		if(pInfo.S_Line.equals("���ں���"))
			{
			for(int i=0;i<bInfo.size();i++) {
				if(bInfo.get(i).compulateORnot) {
					if(bInfo.get(i).weight>50||bInfo.get(i).weight<=0||
							bInfo.get(i).height+info.get(i).length+info.get(i).width>200||
							bInfo.get(i).height+info.get(i).length+info.get(i).width<0||
							bInfo.get(i).height<=0||bInfo.get(i).length<=0||bInfo.get(i).width<=0) {
						return -1;
					}
				}
			}
		}
		else {
			//System.out.println("binfo.size"+bInfo.size());
			for(int i=0;i<bInfo.size();i++) {
				//System.out.println("i"+"+"+i);
				if(bInfo.get(i).compulateORnot) {
					if(bInfo.get(i).weight>45||
							bInfo.get(i).weight<=0||
							bInfo.get(i).height+bInfo.get(i).length+bInfo.get(i).width>300||
							bInfo.get(i).height+bInfo.get(i).length+bInfo.get(i).width<0||
							bInfo.get(i).height<=0||bInfo.get(i).length<=0||bInfo.get(i).width<=0) {
						return -1;
					}
				}
			}
		}
		if(pInfo.S_Line.equals("���ں���"))res=Compute_Inline(pInfo.S_Cabin,pInfo.S_Type,pInfo.current_fare);
		else if(pInfo.S_Line.equals("���ʺ���һ"))res=Compute_Outline_1(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("���ʺ��߶�"))res=Compute_Outline_2(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("���ʺ�����"))res=Compute_Outline_3(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("�漰����ʼ�����й���ĺ���"))res=Compute_Outline_4(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("�漰����/��³ľ����ϰ�֮��ĺ���"))res=Compute_Outline_5(pInfo.S_Cabin,pInfo.S_Type);
		else res=Compute_Outline_6(p_info.S_Cabin,p_info.S_Type);
		
		//info.clear();
		String res_s=String.valueOf(res);
		System.out.println(res_s);
		//request.setAttribute("Money",res_s);
		//request.getRequestDispatcher("/Index2.jsp").forward(request, response);
    	return res;
    }
	
}

class Baggage_INFO{
	//float count;
	public Baggage_INFO(int w,int h,int l,int k,boolean c) {
		width=w;
		height=h;
		length=l;
		weight=k;
		compulateORnot=c;
	}
	public Baggage_INFO() {
		
	}
	public int width;
	public int height;
	public int length;
	public int weight;
	boolean compulateORnot;
}
class Passenger_Info{
	public Passenger_Info(String t,String c,String l,int f) {
		S_Type=t;
		S_Cabin=c;
		S_Line=l;
		current_fare=f;
	}
	public Passenger_Info() {
		
	}
	String S_Type;//�ÿ�����
	String S_Cabin;//��λ����
	String S_Line;//��������
	int current_fare;//Ʊ��
	
}
class TestNG{
	
}
