package Snake;


import java.awt.*;

public class SnakeRunner {
    public static void main(String[] args){

        EventQueue.invokeLater(() ->
        {
            Snake snake = Snake.getInstance();
        });
    }
}

/*  Rzeczy dotyczące programu
algorytm poruszania się węża:
    W każdym zdarzeniu które odpala Timer.
    na podstawie zmiennej direction sprawdzamy czy ruch w tym kierunku nie spowoduje wyjścia poza ramke.
    jeśli nie spowoduje wyjścia poza ramke, przestawiamy głowe węża w danym kierunku.
    następnie dodajemy nową głową węża na koniec kolekcji snakesPart,
    oraz usuwamy ostatni kwadracik węża(ogon).

TODO: w programie istnieje bug polegający na tym że:
    - Wonsz rzeczny niebezpieczny jedzie w prawo (lub jakimkolwiek innym kierunku).
    - W tym czasie my wciśniemy szybko kombinacje guziczków W, A lub S, A.
    - to wtedy wonsz zacznie jechać odrazu w lewo, nie wykona ruchu do góry tylko odrazu w lewo.
 EDIT: naprawione


TODO: górny pasek ramki zjada nam 30 pixeli okienka, trzeba o tym pamiętać przy wyświetlaniu węża i ciastek.
      żeby nie wyświetlić coś poniżej ramki.
 EDIT: naprawione, została dodana stała przechowująca tą informacje

TODO: w algorytmie sprawdzania kolizji z ciałem należy pominąć sprawdzanie ostatneigo elementu węża!
      kiedy wąż tworzy prostokąt i pójdzie do przodu, to jego tył też się przesunie i nie będzie kolizji.
      dlatego pomijamy ostatni tył w tym algorytmie
 EDIT: zrobione

TODO: kiedy wystapi kolizja zmienić kolor węża na czerwony
 EDIT: zrobione

TODO: trzeba zrobić zeby ciasteczko nie mogło pojawić się na wężu

TODO: trzeba zrobic z lagami bo zamula ten wonsz...
 */