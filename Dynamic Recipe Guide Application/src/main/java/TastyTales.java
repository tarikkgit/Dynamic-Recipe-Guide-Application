
import tastytales.Administrator;
import tastytales.Customer;
import tastytales.Nationality;
import tastytales.Recipe_type; 
import tastytales.Recipes;
import tastytales.Requests;
import tastytales.Status_type;

import java.util.Scanner;//for input
import java.util.ArrayList;//for store information
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;// for current time and date

public class TastyTales {

    static ArrayList<Recipes> RecL = new ArrayList<>();//store Recipes
    static ArrayList<Customer> CustL = new ArrayList<>();//store Customers
    static ArrayList<Administrator> AdminL = new ArrayList<>(); //store Administrators
    static ArrayList<Nationality> NatL = new ArrayList<>(); //store Nationality
    static ArrayList<Requests> ReqL = new ArrayList<>();//store Requests
    static ArrayList<Recipe_type> RTL = new ArrayList<>();//store Recipes type
    static ArrayList<Status_type> STL = new ArrayList<>();//store Status type


    public static void addnewCustomerM(String entdate)
    {
        int nat ;
        String usern, pass, fullname, pho, dobi, pobi, eml  ;

        Scanner choseObjS = new Scanner(System.in);
        Scanner choseObj = new Scanner(System.in);

        try
        {
            System.out.println("Butun bilgilerinizin girisini yapin ");
            System.out.println("Kullanici adinizi girin: ");
            usern = choseObjS.nextLine();
            System.out.println("Gercek isminizi girin: ");
            fullname = choseObjS.nextLine();
            System.out.println("Sifrenizi girin:");
            pass = choseObjS.nextLine();
            System.out.println("Telefon numaranızı girin: ");
            pho = choseObjS.nextLine();
            System.out.println("DOB yi girin:");
            dobi = choseObj.nextLine();
            System.out.println("POB yi girin:");
            pobi = choseObjS.nextLine();
            System.out.println("Milletinizi Girin: ");
            System.out.println("Milletinizin ID :   Milletinizin Tanimi : "  );
            for (int i=0 ; i<NatL.size();i++)
            {
                NatL.get(i).NationalityPrint();
            }
            nat = choseObjS.nextInt();
            System.out.println("Email adresinizi girin: ");
            eml = choseObj.nextLine();
            CustL.add(  new Customer (usern, fullname, pass,  eml, nat, pho, dobi, pobi));
            System.out.println("Musteri girisi tamamlandi...");
        }
        catch (Exception e)
        {
            System.out.println("Hata Olustu: " + e.getMessage());
        }
    }





    public static void addnewRecipeM(String entdate)
    {
        int   Rec_id, Rec_type;
        String Rec_desc,Rec_img_sr,Rec_components, Rec_details;


        Scanner choseObjREC = new Scanner(System.in);
        Scanner choseObjREC2 = new Scanner(System.in);
        try
        {


            Rec_id =RecL.size()+1;
            System.out.println("Tarifinizin adini girin: ");
            Rec_desc = choseObjREC.nextLine();

            System.out.println("Tarifinizin resmini girin: ");
            Rec_img_sr = choseObjREC.nextLine();
            System.out.println("Tarifinizin turunu girin: ");
            System.out.println("ID      Tipi ");
            for (int j=0 ; j<RTL.size();j++)
            {
                RTL.get(j).Recipe_typePrint();
            }
            Rec_type = choseObjREC2.nextInt();
            System.out.println("Tarifinizin bilesenlerini girin: ");
            Rec_components = choseObjREC.nextLine();
            System.out.println("Tarifinizin detaylarini girin: ");
            Rec_details = choseObjREC.nextLine();
            System.out.println("Tamam");//
            RecL.add(new Recipes(Rec_id, Rec_desc ,entdate, Rec_img_sr, Rec_components, Rec_type, Rec_details));
            System.out.println("Tarif eklemesi tamamlandi");
        }
        catch (Exception e)
        {
            System.out.println("Bir hata olustu: " + e.getMessage());
        }
    }

    public static void addnewRequestM(String entdate)
    {
        int Rec_type, Req_id, Req_status;
        String Req_answer, Req_customer, Req_desc;

        Scanner choseObjREQ = new Scanner(System.in);
        Scanner choseObjREQ2 = new Scanner(System.in);
        Scanner choseObjREQ3 = new Scanner(System.in);

        try
        {
            Req_id = ReqL.size()+1;
            System.out.println("Istek taniminizi girin:");
            Req_desc = choseObjREQ2.nextLine();
            Req_status=1;
            System.out.println("Tuketicinin istegini girin:");
            Req_customer = choseObjREQ3.nextLine();
            System.out.println("Tarif turunu girin: ");
            System.out.println("ID      Tipi ");
            for (int j=0 ; j<RTL.size();j++)
            {
                RTL.get(j).Recipe_typePrint();
            }
            Rec_type = choseObjREQ2.nextInt();
            System.out.println("Istek cevabini girin:");
            Req_answer = choseObjREQ3.nextLine();
            System.out.println("Tamam");
            ReqL.add(new Requests(Req_id, Req_desc,entdate,Req_status, Req_answer, Req_customer, Rec_type));
        }
        catch (Exception e)
        {
            System.out.println("Bir hata olustu: " + e.getMessage());
        }
    }
    public static void main(String[] args)
    {

        int chose,chose1,chose2, chose3, chose4, chose5, chose6,  naid, exitno, current;
        String usern, pass, fullname, pho,  eml, nadesc  ;


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String entdate = dtf.format(now);



        System.out.println("ArrayLists Tanimlandi : ");

        // add 4 nationality
        NatL.add(new Nationality(1, "Turkiye",  entdate));
        NatL.add(new Nationality(2, "Amerika",  entdate));
        NatL.add(new Nationality(3, "Almanya",  entdate));
        NatL.add(new Nationality(4, "Japonya",  entdate));
        NatL.add(new Nationality(5, "Guney Kore",  entdate));//
        //add recipe type
        RTL.add(new Recipe_type( 1,"Kahvalti", entdate));
        RTL.add(new Recipe_type( 2,"Ogle Yemegi", entdate));
        RTL.add(new Recipe_type( 3,"Aksam Yemegi", entdate));
        RTL.add(new Recipe_type( 4,"Tatli", entdate));
        RTL.add(new Recipe_type( 5,"Atistirmaliklar", entdate));

        //add status type
        STL.add(new Status_type (1,"new Entered",entdate));
        STL.add(new Status_type (2,"in Progress",entdate));
        STL.add(new Status_type (1,"Compleate",entdate));

        //add recipe
        RecL.add(new Recipes(1,"Salset al tomato",entdate,"c://","tomato",1,"tomato"));

        //add request
        ReqL.add(new Requests(1,"tomato",entdate,1,"not yet","hashim",1));

        //add demo administrator
        AdminL.add(new Administrator("Admin","123","Master","555","h@h.com"));

        //add demo customer
        CustL.add( new Customer("Cust","cust","123","email",3,"","",""));

        Scanner choseObj = new Scanner(System.in);
        Scanner choseObjS = new Scanner(System.in);
        Scanner choseObj1 = new Scanner(System.in);
        Scanner choseObj2 = new Scanner(System.in);
        Scanner choseObjC = new Scanner(System.in);
        Scanner choseObjN = new Scanner(System.in);
        Scanner choseObjL = new Scanner(System.in);
        Scanner choseObjCUST = new Scanner(System.in);
        Scanner exitcode = new Scanner(System.in);

        do {


            System.out.println("Kullanici tipini secin : ");
            System.out.println("1: Admin ");
            System.out.println("2: Tuketici ");
            System.out.println("3: BMI Hesapla");
            System.out.println("4: Ziyaretci");

            chose  = choseObj.nextInt();
            switch (chose)
            {
                case 1:
                {
                    System.out.println("Admin");
                    System.out.println("Tum erisime sahipsiniz");
                    System.out.println("Listeden birini secin : ");
                    System.out.println("1: Giris");
                    System.out.println("2: Kayit olma ");
                    System.out.println("3: Tuketici taktikleri ");
                    System.out.println("4: Tarif metotlari");
                    System.out.println("5: istek metotlari ");
                    System.out.println("6: Millet metotlari ");
                    chose1 = choseObj.nextInt();
                    switch (chose1)
                    {
                        case 1:
                        {
                            current=0;
                            System.out.println("Kullanici ismi : ");
                            usern = choseObjL.nextLine();
                            System.out.println("Sifre :  ");
                            pass = choseObjL.nextLine();
                            for (int i =0; i<AdminL.size();i++)
                            {
                                if(AdminL.get(i).Username.equals(usern) )
                                {
                                    current=i;
                                }
                            }
                            AdminL.get(current).login(usern, pass);

                        }
                        break;
                        case 2:
                        {
                            System.out.println("Kullanici ismi : ");
                            usern = choseObj1.nextLine();
                            System.out.println("Sifre :  ");
                            pass = choseObj2.nextLine();
                            System.out.println("Admin full isim :  ");
                            fullname = choseObj1.nextLine();
                            System.out.println("Telefon no :  ");
                            pho = choseObj2.nextLine();
                            System.out.println("Email :  ");
                            eml = choseObj1.nextLine();
                            AdminL.add( new Administrator(usern,pass,fullname,pho,eml));
                            System.out.println("Admin ekleme basarili");

                        }
                        break;
                        case 3:
                        {
                            System.out.println("1: Tuketici ekle ");
                            System.out.println("2: tuketici goster");
                            chose5 = choseObjC.nextInt();
                            switch (chose5)
                            {
                                case 1:
                                {
                                    addnewCustomerM( entdate);
                                }
                                break;
                                case 2:
                                {
                                    System.out.println("Kullanici adi  |  Sifre    |    Email    | "
                                            + "  Butun isim  |  DOB  |   POB   |  Telefon no | Millet");
                                    for (int i=0 ; i<CustL.size();i++)
                                    {
                                        CustL.get(i).Customerprint();
                                    }

                                }
                                break;
                                default:
                                {
                                    System.out.println("Bu giris listede yok ");
                                }
                            }
                        }
                        break;
                        case 4:
                        {
                            System.out.println("1: Tarif Ekle ");
                            System.out.println("2: Tarif Goster ");
                            chose6 = choseObjC.nextInt();
                            switch (chose6)
                            {
                                case 1:
                                {
                                    addnewRecipeM(entdate);
                                }
                                break;
                                case 2:
                                {
                                    System.out.println("id  |  Ad     | tur  |   img_sr  |  Bilesenler  |   detaylar");
                                    for (int i=0 ; i<RecL.size();i++)
                                    {
                                        RecL.get(i).RecipesPrint();
                                    }
                                }
                                break;
                                default:
                                {
                                    System.out.println("Bu giris listede yok ");
                                }
                            }


                        }
                        break;
                        case 5:
                        {
                            System.out.println("1: istek ekle ");
                            System.out.println("2: istek goster ");
                            chose4 = choseObjS.nextInt();
                            switch (chose4)
                            {
                                case 1:
                                {
                                    addnewRequestM( entdate);
                                }
                                break;
                                case 2:
                                {
                                    System.out.println(" id  | Ad |     Tarih   | Durum   | Cevap         | kim yaptı   | tarif turu");
                                    for (int i=0 ; i<ReqL.size();i++)
                                    {
                                        ReqL.get(i).RequestsPrint();
                                    }
                                }
                                break;
                                default:
                                {
                                    System.out.println("Bu giris listede yok ");
                                }

                            }
                        }
                        break;
                        case 6:
                        {
                            System.out.println("1: Millet ekle ");
                            System.out.println("2:Milletleri goster ");
                            chose3 = choseObj.nextInt();
                            switch (chose3)
                            {
                                case 1:
                                {

                                    naid = NatL.size()+1;
                                    System.out.println("Millet tanimi gir: ");
                                    nadesc = choseObjN.nextLine();
                                    NatL.add(new Nationality(naid, nadesc,  entdate));
                                }
                                break;

                                case 2:
                                {
                                    System.out.println("Millet ID :   Millet tanimi : "  );
                                    for (int i=0 ; i<NatL.size();i++)
                                    {
                                        NatL.get(i).NationalityPrint();
                                    }
                                }
                                break;
                                default:
                                {
                                    System.out.println("Bu giris listede yok");
                                }
                            }
                        }
                        break;
                        default:
                        {
                            System.out.println("Bu giris listede yok ");
                        }
                    }
                    break;
                }
                case 2:
                {
                    System.out.println("Tuketici");
                    System.out.println("Listeden birini sec: ");
                    System.out.println("1: Giris ");
                    System.out.println("2: Kayit ");
                    System.out.println("3: Yeni tarif ekle ");
                    System.out.println("4: Yeni istek ekle ");
                    System.out.println("5: Tarifleri goruntule ");
                    chose2 = choseObj.nextInt();
                    switch (chose2)
                    {
                        case 1:
                        {
                            current=0;
                            System.out.println("Kullanici adini gir  ");
                            usern = choseObjCUST.nextLine();
                            System.out.println("Sifreyi gir:");
                            pass = choseObjCUST.nextLine();
                            for (int i =0; i<CustL.size();i++)
                            {
                                if(CustL.get(i).Username.equals(usern) )
                                {
                                    current=i;
                                }
                            }
                            CustL.get(current).login(usern, pass);

                        }
                        break;
                        case 2:
                        {
                            addnewCustomerM( entdate);
                        }
                        break;
                        case 3:
                        {
                            addnewRecipeM(entdate);
                        }
                        break;
                        case 4:
                        {
                            addnewRequestM( entdate);
                        }
                        break;
                        case 5:
                        {
                            System.out.println("id  |  Ad    | tur |   img_sr  |  Bilesenler  |   detaylar");
                            for (int i=0 ; i<RecL.size();i++)
                            {
                                RecL.get(i).RecipesPrint();
                            }
                        }
                        break;
                        default:
                        {
                            System.out.println("Bu giris listede yok ");
                        }
                    }
                }
                break;
                case 3://Calculate BMI
                {
                    double w,h;
                    System.out.println("Yükseklik : " );
                    w = choseObj.nextDouble();
                    System.out.println("Agirlik : " );
                    h = choseObj.nextDouble();
                    double BMI = CustL.get(0).calculateBMI(w, h);
                    System.out.println("BMI =  " + BMI);
                }
                break;
                case 4:
                {
                    System.out.println("Ziyaretci");
                    System.out.println("Sen sadece listeleri okuyabilirsin");
                    System.out.println("id  |  tanim    | tur  |   img_sr  |  Bilesenler |   detaylar");
                    for (int i=0 ; i<RecL.size();i++)
                    {
                        RecL.get(i).RecipesPrint();
                    }
                }
                break;
                default:
                {
                    System.out.println("Bu giris listede yok ");
                }
            }

            System.out.println(" 99 girersen cikar 1 girersen devam eder");
            exitno  = exitcode.nextInt();
        }
        while (exitno !=99);
    }

}