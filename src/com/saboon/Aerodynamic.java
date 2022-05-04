package com.saboon;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class Aerodynamic {


    private ArrayList<Double> Cd = new ArrayList<>();
    private ArrayList<Double> Cd_0 = new ArrayList<>();
    private ArrayList<Double> Cd_3 = new ArrayList<>();
    private ArrayList<Double> Cd_6 = new ArrayList<>();
    private ArrayList<Double> Cd_12 = new ArrayList<>();
    private ArrayList<Double> F_Thrust = new ArrayList<>();

    public ArrayList<Double> getCd() {
        return Cd;
    }

    public ArrayList<Double> getCd_0() {
        return Cd_0;
    }

    public ArrayList<Double> getCd_3() {
        return Cd_3;
    }

    public ArrayList<Double> getCd_6() {
        return Cd_6;
    }

    public ArrayList<Double> getCd_12() {
        return Cd_12;
    }

    public ArrayList<Double> getF_Thrust() {
        return F_Thrust;
    }

    public Aerodynamic() {


//        initializeCdLavira();
//        initializeTfLavira();

        initializeCdTekno();
        inititalizeTfTekno();


    }

    public void initializeCdTekno() {
        Cd_0.add(0.434);
        Cd_0.add(0.3954);
        Cd_0.add(0.3752);
        Cd_0.add(0.3617);
        Cd_0.add(0.3515);
        Cd_0.add(0.3429);
        Cd_0.add(0.3359);
        Cd_0.add(0.3309);
        Cd_0.add(0.3293);
        Cd_0.add(0.37);
        Cd_0.add(0.4111);
        Cd_0.add(0.3996);
        Cd_0.add(0.3995);
        Cd_0.add(0.3908);
        Cd_0.add(0.382);
        Cd_0.add(0.3696);
        Cd_0.add(0.3575);
        Cd_0.add(0.3461);
        Cd_0.add(0.335);
        Cd_0.add(0.3244);

        Cd_3.add(0.4512);
        Cd_3.add(0.4099);
        Cd_3.add(0.3885);
        Cd_3.add(0.3741);
        Cd_3.add(0.3633);
        Cd_3.add(0.3542);
        Cd_3.add(0.3468);
        Cd_3.add(0.3415);
        Cd_3.add(0.3398);
        Cd_3.add(0.3805);
        Cd_3.add(0.4205);
        Cd_3.add(0.4083);
        Cd_3.add(0.4079);
        Cd_3.add(0.399);
        Cd_3.add(0.39);
        Cd_3.add(0.3773);
        Cd_3.add(0.3651);
        Cd_3.add(0.3535);
        Cd_3.add(0.3423);
        Cd_3.add(0.3315);

        Cd_6.add(0.4711);
        Cd_6.add(0.4267);
        Cd_6.add(0.4036);
        Cd_6.add(0.3882);
        Cd_6.add(0.3767);
        Cd_6.add(0.367);
        Cd_6.add(0.3592);
        Cd_6.add(0.3536);
        Cd_6.add(0.3519);
        Cd_6.add(0.3924);
        Cd_6.add(0.4313);
        Cd_6.add(0.4181);
        Cd_6.add(0.4175);
        Cd_6.add(0.4083);
        Cd_6.add(0.3991);
        Cd_6.add(0.3862);
        Cd_6.add(0.3738);
        Cd_6.add(0.362);
        Cd_6.add(0.3506);
        Cd_6.add(0.3396);

        Cd_12.add(0.5249);
        Cd_12.add(0.4715);
        Cd_12.add(0.4441);
        Cd_12.add(0.4259);
        Cd_12.add(0.4124);
        Cd_12.add(0.4012);
        Cd_12.add(0.3923);
        Cd_12.add(0.3858);
        Cd_12.add(0.3838);
        Cd_12.add(0.424);
        Cd_12.add(0.4598);
        Cd_12.add(0.4443);
        Cd_12.add(0.443);
        Cd_12.add(0.4331);
        Cd_12.add(0.4233);
        Cd_12.add(0.4098);
        Cd_12.add(0.3968);
        Cd_12.add(0.3844);
        Cd_12.add(0.3725);
        Cd_12.add(0.361);
    }

    public void initializeCdLavira(){
        try {

            File file = new File("C:\\Users\\Sabahattin\\Desktop\\Lavira Rocket\\documents\\ucus benzetim raporlari\\codes\\sources\\time-mach-Cd.xlsx");
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file


            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object



            Iterator<Row> rowItr = sheet.iterator(); // iterating over excel file


            int previousRowNum = 0;
            int rowNum;
            int timeCellNum = 0;
            int CdCellNum = 2;

            while (rowItr.hasNext()) {

                Row row = rowItr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column

                rowNum = row.getRowNum();

                XSSFCell previousRowTime = sheet.getRow(previousRowNum).getCell(timeCellNum);
                XSSFCell rowTime = sheet.getRow(rowNum).getCell(timeCellNum);

                XSSFCell previousRowCd = sheet.getRow(previousRowNum).getCell(CdCellNum);
                XSSFCell rowCd = sheet.getRow(rowNum).getCell(CdCellNum);


                ArrayList<Double> interValues = new ArrayList<>();
                double previousTime = previousRowTime.getNumericCellValue();
                double previousCd = previousRowCd.getNumericCellValue();
                double currentTime = rowTime.getNumericCellValue();
                double currentCd = rowCd.getNumericCellValue();

                //Cd.add(currentCd);

                interValues = interpolation(
                        change(check(previousTime), 2),
                        previousCd,
                        change(check(currentTime), 2),
                        currentCd
                );

                if (interValues != null){
                    if (!interValues.isEmpty()){
                        Cd.addAll(interValues);
                    }
                }else {
                    Cd.add(0.0);
                }
                previousRowNum = rowNum;




            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void inititalizeTfTekno(){
        try {
            File file = new File("C:\\Users\\Sabahattin\\Desktop\\Lavira Rocket\\documents\\ucus benzetim raporlari\\codes\\sources\\veri_itki_F_2022.xlsx");
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file


            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object



            Iterator<Row> rowItr = sheet.iterator(); // iterating over excel file


            int previousRowNum = 0;
            int rowNum;
            int timeCellNum = 0;
            int CdCellNum = 1;

            while (rowItr.hasNext()) {

                Row row = rowItr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column


                rowNum = row.getRowNum();

                XSSFCell previousRowTime = sheet.getRow(previousRowNum).getCell(timeCellNum);
                XSSFCell rowTime = sheet.getRow(rowNum).getCell(timeCellNum);

                XSSFCell previousRowFt = sheet.getRow(previousRowNum).getCell(CdCellNum);
                XSSFCell rowFt = sheet.getRow(rowNum).getCell(CdCellNum);

                F_Thrust.add(rowFt.getNumericCellValue());
                previousRowNum = rowNum;
            }
        }catch (Exception e){

        }
    }

    public void initializeTfLavira(){
        try {

            File file = new File("C:\\Users\\Sabahattin\\Desktop\\Lavira Rocket\\documents\\ucus benzetim raporlari\\codes\\sources\\itki_degerleri.xlsx");
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file


            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object



            Iterator<Row> rowItr = sheet.iterator(); // iterating over excel file


            int previousRowNum = 0;
            int rowNum;
            int timeCellNum = 0;
            int CdCellNum = 1;

            while (rowItr.hasNext()) {

                Row row = rowItr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column


                rowNum = row.getRowNum();

                XSSFCell previousRowTime = sheet.getRow(previousRowNum).getCell(timeCellNum);
                XSSFCell rowTime = sheet.getRow(rowNum).getCell(timeCellNum);

                XSSFCell previousRowFt = sheet.getRow(previousRowNum).getCell(CdCellNum);
                XSSFCell rowFt = sheet.getRow(rowNum).getCell(CdCellNum);



                ArrayList<Double> interValues = new ArrayList<>();
                double previousTime = previousRowTime.getNumericCellValue();
                double previousFt = previousRowFt.getNumericCellValue();
                double currentTime = rowTime.getNumericCellValue();
                double currentFt = rowFt.getNumericCellValue();
                interValues = interpolation(
                        change(check(previousTime), 2),
                        previousFt,
                        change(check(currentTime), 2),
                        currentFt
                );

                if (interValues != null){
                    if (!interValues.isEmpty()){
                        F_Thrust.addAll(interValues);
                    }
                }
                previousRowNum = rowNum;
            }
        }catch (Exception e){

        }
    }




    public ArrayList<Double> interpolation(double previousTime, double previousValue, double currentTime, double currentValue){

        if (currentValue != 0){
            ArrayList<Double> interValues = new ArrayList<>();

            int count = (int) ((currentTime - previousTime) / 0.01 - 1);
            double slope = (currentValue - previousValue)/(currentTime - previousTime);
            double interNextCd;

            for (int i = 0; i<count; i++){
                interNextCd = slope * 0.01 + previousValue;
                interValues.add(interNextCd);
                previousValue = interNextCd;
            }
            interValues.add(currentValue);
            return interValues;
        }

        return null;
    }

    static double change(double value, int decimalpoint)
    {
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);

        return value;
    }

    static double check(double time){
        if (time>1000){
            double t = time/10000;
            return t;
        }
        return time;
    }
}
