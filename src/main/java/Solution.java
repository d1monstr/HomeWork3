/*
Домашнее задание №3
	На входе имеется файл в формате json, содержащий информацию о каком-то количестве организаций, в т.ч. названия, адреса, номера телефонов, ИНН, ОГРН, а также о ценных бумагах, которыми владеет каждая компания.
	Необходимо сформировать на основе исходного файла коллекцию объектов без потери информации, где каждый объект представляет одну организацию.

Для сформированной коллекции:

	-Вывести все имеющиеся компании в формате «Краткое название» – «Дата основания 17/01/98»;

	-Вывести все ценные бумаги (их код, дату истечения и полное название организации-владельца), 		которые просрочены на текущий день, а также посчитать суммарное число всех таких бумаг;

	-На запрос пользователя в виде даты «ДД.ММ.ГГГГ», «ДД.ММ,ГГ», «ДД/ММ/ГГГГ» и «ДД/ММ/ГГ» вывести 		название и дату создания всех организаций, основанных после введенной даты;

	-На запрос пользователя в виде кода валюты, например EU, USD, RUB и пр. выводить id и коды ценных 	бумаг, использующих заданную валюту.
*/


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        Gson g = new Gson();
        Solution solution = new Solution();
        // Составляем jsonString
        String fileName = "src\\main\\resources\\info.json";
        String jsonString = solution.createJsonString(fileName);
        //
        // Составляем список организаций
        Type itemsListType = new TypeToken<List<Organization>>() {}.getType();
        List<Organization> listOrganizations = g.fromJson(jsonString, itemsListType);
        //
        // Выводим краткую информацию о компаниях
        solution.shortInfoOrg(listOrganizations);
        //
        System.out.println();
        // Выводим информацию о просроченных ценных бумагах
        solution.overdueSecurities(listOrganizations);
        //
        System.out.println();
        // Выводим организации основанные после указанной даты
        solution.orgAfter(listOrganizations, "01/01/1996");
        //
        System.out.println();
        // Выводим id и коды ценных бумаг по заданному коду валюты
        solution.printSecuritiesOf(listOrganizations, "rub");
        //


    }

    public void printSecuritiesOf(List<Organization> list, String codeCurrency){
        for (Organization organization : list){
            for (Security security : organization.getSecurities()){
                if (codeCurrency.toLowerCase().equals(security.getCodeСurrency().toLowerCase())){
                    System.out.println("Id: " + security.getId() + " Код: " + security.getCode());
                }
            }
        }
    }

    public void orgAfter(List<Organization> list, String date){
        List<DateFormat> dateFormatList = new ArrayList<DateFormat>();
        dateFormatList.add(new SimpleDateFormat("dd.MM.yyyy"));
        dateFormatList.add(new SimpleDateFormat("dd.MM,yy"));
        dateFormatList.add(new SimpleDateFormat("dd/MM/yyyy"));
        dateFormatList.add(new SimpleDateFormat("dd/MM/yy"));

        Date dateFormatted = null;
        for (Organization organization : list){
            try {
                dateFormatted = dateFormatList.get(0).parse(date);
            }
            catch (ParseException e1){
                try {
                    dateFormatted = dateFormatList.get(1).parse(date);
                }
                catch (ParseException e2){
                    try {
                        dateFormatted = dateFormatList.get(2).parse(date);
                    }
                    catch (ParseException e3){
                        try {
                            dateFormatted = dateFormatList.get(3).parse(date);
                        }
                        catch (ParseException e4){
                            System.out.println("Формат даты указан некорректно!");
                        }
                    }
                }
            }
            try {
                if (dateFormatted.compareTo(dateFormatList.get(0).parse(organization.getCreateDate())) < 0){
                    System.out.println(organization.getOrganizationName() + " - " + dateFormatList.get(3).format(dateFormatList.get(0).parse(organization.getCreateDate())));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void overdueSecurities(List<Organization> list){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int count = 0;
        for (Organization organization : list){
            for (Security security : organization.getSecurities()){
                try {
                    if (date.compareTo(dateFormat.parse(security.getEndDate())) > 0){
                        count++;
                        System.out.println("Код: " + security.getCode() + " Дата истечения: " + security.getEndDate() + " Организация: " + organization.getOrganizationName());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        System.out.println("Всего просрочено: " + count);
    }

    public void shortInfoOrg(List<Organization> list){
        for (Organization organization : list){
            String dateString = organization.getCreateDate();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            DateFormat startDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            try {
                System.out.println(organization.getOrganizationName() + " - " + dateFormat.format(startDateFormat.parse(dateString)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String createJsonString(String fileName){
        String jsonString = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            int data = bufferedReader.read();

            while (data != -1){
                jsonString += (char) data;
                data = bufferedReader.read();
            }
        }
        catch (IOException e){
        }
        return jsonString;
    }




}
