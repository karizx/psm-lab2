# PSM 2026/2027 - Laboratorium 2: Tryb portretowy i przelicznik miar

**Autor:** Karol Budzyń  
**Kierunek:** Informatyka (Inżynieria Systemów Inteligentnych)  
**Uczelnia:** Akademia Tarnowska

## Cel projektu
Celem zadania było stworzenie aplikacji przeliczającej na bieżąco długość w metrach na inne jednostki, przy jednoczesnym uwzględnieniu dobrych praktyk inżynierskich: separacji logiki od interfejsu (Jetpack Compose), testów jednostkowych (JUnit), obsługi sytuacji skrajnych (puste pole, tekst niebędący liczbą, skrajne rzędy wielkości) oraz dodania własnej ikony adaptacyjnej.

## Odpowiedzi na pytania ze sprawozdania

### 1. Blokada orientacji a duże ekrany
Zgodnie z poleceniem do pliku manifestu dodano atrybut `android:screenOrientation="portrait"`.
* **Na telefonie:** Aplikacja ściśle trzyma się orientacji pionowej (nie obraca się przy fizycznym obrocie emulatora).
* **Na tablecie:** System zignorował atrybut i obrócił aplikację do poziomu.
* **Wyjaśnienie różnicy:** Począwszy od Androida 12L, a w szczególności dla aplikacji targetujących Android 17 (API 37), system całkowicie ignoruje atrybuty blokujące orientację na urządzeniach, których najmniejszy bok wynosi co najmniej 600 dp (czyli tabletach, urządzeniach składanych oraz w trybie desktopowym). Zmiana ta została wprowadzona przez Google, aby zapobiec tzw. *letterboxingowi* (wyświetlaniu aplikacji w wąskim pasku z czarnymi ramkami po bokach), co drastycznie psuło UX na dużych ekranach i urządzeniach fizycznie zablokowanych w poziomie (np. po podłączeniu klawiatury).
* **Co powinno zastąpić blokowanie orientacji?** Zamiast sztucznie blokować ekran, programista powinien stosować **układy adaptacyjne (Adaptive Layouts)** oparte na tzw. klasach rozmiaru okna (*Window Size Classes*). Pozwala to interfejsowi dynamicznie przebudować się w zależności od dostępnego miejsca (np. zmiana układu z jednokolumnowego na wielokolumnowy/siatkowy).

### 2. Uzasadnienie sposobu formatowania wyników
Ponieważ przeliczniki potrafią generować liczby o skrajnych rzędach wielkości (np. rok świetlny to rząd $10^{-16}$, a centymetry często idą w setki tysięcy), wprowadzono progi formatowania w klasie `ConverterLogic`:
* **Wartości standardowe** (od $10^{-3}$ do $10^6$): Wyświetlane są w tradycyjnym zapisie (np. `150,000` dla centymetrów), gdzie zastosowano separator tysięcy i kropkę dziesiętną, co poprawia czytelność ułamków i dużych dystansów na co dzień.
* **Wartości skrajne** ($< 10^{-3}$ oraz $\ge 10^6$): Automatycznie przechodzą w notację wykładniczą (np. `1.5855 × 10^-13` dla lat świetlnych), co zapobiega wyświetlaniu bezużytecznych ciągów zer pokroju `0.00000000000015` lub obcięciu wyniku do samego zera przy sztywnym limicie miejsc po przecinku.

### 3. Realizacja zadania dodatkowego
Zrealizowano pierwsze zadanie dodatkowe polegające na dodaniu własnej, piątej jednostki miary – **Cali (Inches)**.
Dzięki rozdzieleniu logiki (wzorzec ze zbiorem danych opartym na `enum class LengthUnit`) od interfejsu (renderowanie wyników dynamicznie w pętli `LengthUnit.entries.forEach`), dodanie nowej jednostki wymagało zmiany tylko w **jednym** miejscu w kodzie: dopisania rekordu w enumie. Interfejs graficzny uwzględnił ją automatycznie.

## Załączniki
W głównym katalogu repozytorium znajdują się wymagane zrzuty ekranu:
1. `stan_pusty.png` / `stan_wyniki.png` / `stan_blad.png`/ `dodatkowa_jednostka.png` – działanie interfejsu (zadanie 3) oraz zadanie dodatkowe (dodana jednostka Cali).
2. `ikona.png` – własna ikona adaptacyjna (zadanie 1).
3. `telefon_portret.png` / `tablet_poziom.png` – weryfikacja ignorowania blokady orientacji (zadanie 2).
4. `testy_jednostkowe.png` – zielony pasek z uruchomienia weryfikacji logiki bez uruchamiania emulatora (3 tests passed).