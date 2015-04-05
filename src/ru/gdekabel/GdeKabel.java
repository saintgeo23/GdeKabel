package ru.gdekabel;

import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




/**
 * Created by saintgeo23 on 19.03.2015.
 * Моя первая программа вне учебного курса.
 * Парсит определенные страницы с целью записи таблицы в .csv файл
 */
public class GdeKabel {

    public static void main(String[] args) throws IOException {

        Document doc;

        File file = new File("gdekabel.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));


        for (int i = 1;; i++) { // листаем страницы
            String path = "http://www.rkb.ru/cablesearch/?filial=&pattern=%D0%B0%D1%81%D0%B1&threads=&cs_square=&p=" + i;

            try {

                doc = Jsoup.connect(path).get();
                Element table = doc.select("table.items").get(0).child(0);
                Elements rows = table.select("tr");
                if (rows.size() == 1) { // если на странице таблица состоит из одной строки (шапки), покидаем цикл
                    break;
                }
                int n = 0; // переменная, чтобы убрать первую строку в таблице на каждой странице

                for (Element row : rows) {

                    if (n == 0) {
                        n++;
                        continue;
                    }

                    else {
                        Elements tds = row.select("td");
                        int j = 0; // переменная, чтобы правильно расставить ";"
                        String line = "";
                        for (Element td : tds) {
                            j++;
                            if (td.text().equals("Заказать")) { // распознаем конец строки, отсеиваем столбец со словом "Заказать" и переводим курсор на следующую строку
                                line = line + "\n";
                                continue;
                            } else if (j == 1)
                                line = line + td.text();
                            else
                                line = line + ";" + td.text();

                        }
                        System.out.println(line);
                        bw.write(line);
                        n++;
                    }

                }

            }
            catch (IOException e) {
                System.out.println("Something's gone wrong =(");
            }

        }
        bw.close();

    }
}
