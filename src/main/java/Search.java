import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private ArrayList<Baggage_INFO>info;//存储行李的参数
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
		else if(buttonType.equals("ADD")) {//添加行李
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
				String temp="行李:"+String.valueOf(i+1)+" 重量："+String.valueOf(info.get(i).weight)+"KG";
				res+=temp;
				temp=String.valueOf(info.get(i).length)+"CM";
				res=res+"长："+temp;
				temp=String.valueOf(info.get(i).width)+"CM";
				res=res+"宽："+temp;
				temp=String.valueOf(info.get(i).height)+"CM";
				res=res+"高："+temp;
				res+="@";
			}
			request.setAttribute("Info",res);
			request.getRequestDispatcher("/Index2.jsp").forward(request, response);
		}
		else if(buttonType.equals("Search")) {//查询运费
			
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
		if(S_Type.equals("南航明珠金卡会员或天合联盟超级精英")) {
			total_weight=total_weight-20;
		}
		else if(S_Type.equals("南航明珠银卡会员或天合联盟精英")) {
			total_weight=total_weight-10;
		}
		if(S_Cabin.equals("头等舱")) {
			total_weight=total_weight-40;
		}
		else if(S_Cabin.equals("公务舱")) {
			total_weight=total_weight-30;
		}
		else if(S_Cabin.equals("经济舱")||S_Cabin.equals("明珠经济舱")) {
			total_weight=total_weight-20;
		}
		else if(S_Type.equals("婴儿")||S_Type.equals("不占座婴儿")){
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
		if(S_Cabin.equals("头等舱")||S_Cabin.equals("公务舱")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("公务舱")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//头等舱\公务舱免费行李额
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("明珠经济舱")||S_Cabin.equals("经济舱")) {
			int flag1=2;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
				
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
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
		if(S_Cabin.equals("公务舱")) {
			flag1--;
			over_count++;
		}
		if(S_Cabin.equals("明珠经济舱")||S_Cabin.equals("经济舱")) {
			flag1=flag1-2;
			over_count=over_count+2;
		}
		if(S_Type.equals("普通旅客")||S_Type.equals("儿童、不占座婴儿")) {
			over_count++;
			flag2=0;
		}
		if(S_Type.equals("留学生、劳务、海员 ")) {
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
					S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
				if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
					total_price+=1000;
				}
				flag2--;
			}
			else if(info.get(i).weight<=10&&flag2==1&&
					S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
				if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
					total_price+=1000;
				}
				flag2--;
			}
			else if(info.get(i).weight<=32&&flag1>0) {//
				if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
					total_price+=1000;
				}
				flag1--;
			}
			else{//判断超重与超尺寸
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
		if(S_Cabin.equals("头等舱")||S_Cabin.equals("公务舱")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("公务舱")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//头等舱\公务舱免费行李额
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("明珠经济舱")||S_Cabin.equals("经济舱")) {
			int flag1=2;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
				
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
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
		
		if(S_Cabin.equals("头等舱")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Type.equals("留学生、劳务、海员")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
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
						S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=10&&flag2==1&&
						S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=32&&flag1>0) {//
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
						total_price+=1000;
					}
					flag1--;
				}
				else{//判断超重与超尺寸
					if(info.get(i).weight>32) {
						total_price+=3000;
					}
					if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
						total_price+=1000;
					}
				}
			}
		}
		else {//公务舱、明珠经济舱、经济舱
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Type.equals("留学生、劳务、海员")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
			}
			if(S_Cabin.equals("明珠经济舱")) {
				flag1--;
				over_count++;
			}
			if(S_Cabin.equals("经济舱")) {
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
						S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=10&&flag2==1&&
						S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
						total_price+=1000;
					}
					flag2--;
				}
				else if(info.get(i).weight<=23&&flag1>0) {//
					if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
						total_price+=1000;
					}
					flag1--;
				}
				else{//判断超重与超尺寸
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
		if(S_Cabin.equals("头等舱")||S_Cabin.equals("公务舱")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("公务舱")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//头等舱\公务舱免费行李额
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
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
		else if(S_Cabin.equals("明珠经济舱")||S_Cabin.equals("经济舱")) {
			int flag1=2;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
				
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
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
		if(S_Cabin.equals("头等舱")||S_Cabin.equals("公务舱")) {
			int flag1=3;
			int flag2=1;
			int over_count=total_count-4;
			if(S_Cabin.equals("公务舱")) {
				flag1=2;
				flag2=1;
				over_count+=1;
			}
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=32&&flag1>0) {//头等舱\公务舱免费行李额
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
						if(info.get(i).weight>32) {
							total_price+=3000;
						}
						if(info.get(i).height+info.get(i).length+info.get(i).width>158) {
							total_price+=1000;
						}
					}
				}
		}
		else if(S_Cabin.equals("明珠经济舱")||S_Cabin.equals("经济舱")) {
			int flag1=1;
			int flag2=1;
			int over_count=total_count-3;
			if(S_Type.equals("留学生、劳务、海员 ")) {
				flag1++;
				flag2--;
			}
			if(S_Type.equals("普通旅客")||S_Type.equals("儿童、占座婴儿")) {
				flag2--;
				over_count++;
			}
			if(over_count>0) {
				total_price=total_price+over_count*2000-1000;
			}
			for(int i=0;i<info.size();i++) {
				if(!info.get(i).compulateORnot)continue;
					if(info.get(i).weight<=20&&flag2==1&&
							S_Type.equals("南航明珠金卡会员、天合联盟超级精英")) {//附赠的免费行李
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=10&&flag2==1&&
							S_Type.equals("南航明珠银卡会员、天合联盟精英")) {
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//附赠但超尺寸
							total_price+=1000;
						}
						flag2--;
					}
					else if(info.get(i).weight<=23&&flag1>0&&
							S_Cabin.equals("经济舱")) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else if(info.get(i).weight<=32&&flag1>0&&
							S_Cabin.equals("明珠经济舱")) {//
						if(info.get(i).height+info.get(i).width+info.get(i).length>158) {//超尺寸
							total_price+=1000;
						}
						flag1--;
					}
					else{//判断超重与超尺寸
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

	public int TestIN(String b,String p,int resS) {
		ArrayList<Baggage_INFO>aOFb=new ArrayList<Baggage_INFO>();
		String []binfo=b.split("@");
		for(int i=0;i<binfo.length;i++) {
			String []temp=binfo[i].split("\\+");
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
		return res;
	}




	public int Testng(ArrayList<Baggage_INFO>bInfo, Passenger_Info pInfo, String[]Baggages) {
    	info=bInfo;
    	if(Baggages==null||Baggages.length==0) {
			return -3;
		}
    	if(pInfo.S_Type.equals("不占座婴儿")) {
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
		if(pInfo.S_Line.equals("国内航线"))
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
			for(int i=0;i<bInfo.size();i++) {
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
		if(pInfo.S_Line.equals("国内航线"))res=Compute_Inline(pInfo.S_Cabin,pInfo.S_Type,pInfo.current_fare);
		else if(pInfo.S_Line.equals("国际航线一"))res=Compute_Outline_1(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("国际航线二"))res=Compute_Outline_2(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("国际航线三"))res=Compute_Outline_3(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("涉及韩国始发与中国间的航线"))res=Compute_Outline_4(pInfo.S_Cabin,pInfo.S_Type);
		else if(pInfo.S_Line.equals("涉及兰州/乌鲁木齐与迪拜之间的航线"))res=Compute_Outline_5(pInfo.S_Cabin,pInfo.S_Type);
		else res=Compute_Outline_6(p_info.S_Cabin,p_info.S_Type);
		String res_s=String.valueOf(res);
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
	String S_Type;//旅客类型
	String S_Cabin;//舱位类型
	String S_Line;//航线类型
	int current_fare;//票价
	
}

