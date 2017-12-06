package com.fxdb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextWorker {

    private final String dataSource = "D:\\DB\\Graffiks\\EURUSD.txt";


    public ArrayList<String[]> readfile (){

        FileReader reader = null;

        ArrayList<String[]> arrayArrayList = new ArrayList<String[]>();

        try {
            reader = new FileReader(dataSource);
            int c;
            String s = "";
            String sDate = "";

            try {
                while((c = reader.read())!=-1){

                    //System.out.print((char)c);

                    if (c == 10){

                        continue;

                    }

                    if (c == 13){

                        String[] sSplited = s.split(",");
                        arrayArrayList.add(sSplited);
                        s = "";
                        continue;

                    }

                    s = s + (char)c;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return arrayArrayList;

    }

}
