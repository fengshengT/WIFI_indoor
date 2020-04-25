package com.example.guoyao.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResualtActivity extends AppCompatActivity {


    private static char[] nullText=null;
    private TextView  mTvAPs;
    private TextView  mTvInfo;



    // 数组转字符串函数
    public static String arrayToString(Object[] array) {
        if (array == null)return "数组为空";// 数组为null，返回空字符串
        int length = array.length;
        if (length == 0) return "[]";// 数组长度为0，返回字符串“[]”
        final String arraySeparator = ",";// 数组元素间的分隔符
        StringBuffer buffer = new StringBuffer("[");
        for (int i = 0; i < length; i++) {// 循环数组，组装字符串
            Object item = array[i];
            buffer.append((item == null)?nullText:item.toString());
            buffer.append(arraySeparator);
        }
        buffer.setCharAt(buffer.length()-1,']');// 替换最后的“，”为“]”
        return buffer.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resualt);


        mTvAPs = findViewById(R.id.tv_info);
        Bundle bundle=getIntent().getExtras();
        int AP1=bundle.getInt("AP1");
        int AP2=bundle.getInt("AP2");


        mTvAPs.setText("序号             坐标             距离    "+AP1+AP2);
        mTvInfo= findViewById(R.id.tv_aps);
        Object[] RS =RSScondiante( AP1, AP2);
        Object[] a =RSScondiante1(AP1, AP2);
        mTvInfo.setText(arrayToString(RS));


    }

    public Object[] RSScondiante1(int AP1, int AP2) {
        int s=AP1+AP2;
        Object[] a={s};
        return a ;
    }


    public static Object[] RSScondiante(int AP1, int AP2) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Rss> DisList = null;
        Object[] RS2 = {1,2};
        //加载驱动类

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testjdbc","root","123456");

            String sql = "select * from coodinate ";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            int num = rs.getMetaData().getColumnCount();

            List<Map<String, Object>> RSSInfo = new ArrayList<Map<String, Object>>();

            while(rs.next()) {
                Map<String, Object> mapOfColValues = new HashMap<String, Object>(num);
                for (int i = 1; i<= num; i++) {
                    //getColunmName获取字段名
                    mapOfColValues.put(rs.getMetaData().getColumnName(i),rs.getObject(i));

                }

                RSSInfo.add(mapOfColValues);
                // System.out.println(mapOfColValues);
                //System.out.println(rs.getInt(1)+"---"+rs.getDouble(2)+"---"+rs.getDouble(3)+"---"+rs.getString(4));
            }
            Object[][] sample3= new Object [RSSInfo.size()][4]  ;
            Map dqMap = new  HashMap(num) ;
            for(int i=0;i<RSSInfo.size();i++){
                dqMap = RSSInfo.get(i);

                sample3 [i][0] =dqMap.get("id");
                sample3 [i][1] =dqMap.get("AP1");
                sample3 [i][2] =dqMap.get("AP2");
                sample3 [i][3] =dqMap.get("coodinate");

            }

            for(Object[] temp:sample3) {
                System.out.println(Arrays.toString(temp));
            }

            Object[] unknrss= { 50,-35,-40, null};
            int length = sample3.length;
            System.out.println("序号             坐标             距离                 ");
             DisList = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                Object[] rsses = sample3[i];
                double distances = getDistance(rsses, unknrss);
                Rss RssDis = new   Rss(rsses[0],  rsses[3], distances);
                // Object[] disInfo={};
                System.out.println(String.format("%s     %s       %s",  rsses[0],  rsses[3], distances));
                DisList.add(RssDis);
            }

            Collections.sort(DisList, new Comparator<Rss>() {

                @Override
                public int compare(Rss r1, Rss r2) {
                    double sub = (r1.getDistance() - r2.getDistance());
                    if (sub == 0) {
                        return 0;
                    }
                    if (sub > 0) {
                        return 1;
                    }
                    return -1;
                }
            });

            int k=1;

		/*	System.out.println("按照欧式距离排序，取k=5");

			DisList=DisList.subList(0,k);
			for (Rss RssDis : DisList) {
				System.out.println(RssDis);
			}
		*/
            System.out.println("由NN算法，取k=1，则当前位置坐标为");

            DisList = DisList.subList(0, k);
            for (Rss RssDis : DisList) {
                System.out.println(RssDis);
            }

            Object[] RS1= {DisList.get(0).getId(),DisList.get(0).getCoodinate(),DisList.get(0).getDistance()};

            return  RS1;


            //System.out.println(RSSInfo);
            //System.out.println(RSSInfo.get(0));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

        return RS2;
    }


    public static double getDistance(Object[] rss1, Object[] rss2) {
        double[] ps1 = { (double) rss1[1], (double) rss1[2] };
        double[] ps2 = { (Integer) rss2[1], (Integer) rss2[2] };
        return getDistance(ps1, ps2);
    }

    public static double getDistance(double[] ps1, double[] ps2) {
        if (ps1.length != ps1.length) {
            throw new RuntimeException("属性数量不对应");
        }
        int length = ps1.length;
        double total = 0;
        for (int i = 0; i < length; i++) {
            double sub = ps1[i] - ps2[i];
            total = total + (sub * sub);
        }
        return Math.sqrt(total);
    }

}


class Rss{
    private Object id;
    private String coodinate;
    private double distance;


    Rss(Object rsses, Object rsses2, double distance) {
        super();
        this.id = rsses;
        this.coodinate = (String) rsses2;
        this.distance = distance;

    }

    @Override
    public String toString() {
        return "RSSDis [id=" + id + ", coodinate=" + coodinate + ", distance=" + distance +"]";
    }


    public Object getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCoodinate() {
        return coodinate;
    }
    public void setCoodinate(String coodinate) {
        this.coodinate = coodinate;
    }
    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }







}










