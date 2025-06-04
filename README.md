# Przydasie

Serwis aukcyjny wykonany w ramach projektu na studia.

## Podział pracy:
Podział pracy w zespole:
### Oskar:
- Dane przechowywane w relacyjnej bazie.
- Testy jednostkowe.
- Dostęp do danych poprzez pulę połączeń skonfigurowaną na serwerze aplikacyjnym.
- Przeglądanie/dodawanie/edycja/usuwanie aukcji.
### Paweł:
- Automatyczne zakończenie aukcji z powiadomieniem autora i osoby, która wygrała aukcję poprzez e-mail.
- Podział na warstwy (osobne komponenty dla warstwy danych - wzorzec DAO, logiki i prezentacji, komunikacja między warstwami z użyciem interfejsów), komponenty warstwy danych i logiki powinny działać w kontenerze serwera aplikacyjnego (EJB/CDI), warstwa prezentacji może być aplikacją webową lub aplikacją kliencką (konsolową lub z GUI działającą jako stand-alone client application w kontenerze aplikacji klienckich).
- Dziennik zdarzeń.
### Hubert:
- Front-end
- Uwierzytelnianie (Obsługa uwierzytelniania).
- Możliwość składania ofert w ramach aukcji.
---
## Wykonane zadania:
### Ogólne wymagania dot. projektu:
- [ ] Dane przechowywane w relacyjnej bazie.
- [ ] Dostęp do danych poprzez pulę połączeń skonfigurowaną na serwerze aplikacyjnym.
- [ ] Podział na warstwy (osobne komponenty dla warstwy danych - wzorzec DAO, logiki i prezentacji, komunikacja między warstwami z użyciem interfejsów), komponenty warstwy danych i logiki powinny działać w kontenerze serwera aplikacyjnego (EJB/CDI), warstwa prezentacji może być aplikacją webową lub aplikacją kliencką (konsolową lub z GUI działającą jako stand-alone client application w kontenerze aplikacji klienckich).
- [ ] Obsługa uwierzytelniania
- [ ] Testy jednostkowe.
- [ ] Dziennik zdarzeń *.

### Funkcjonalność:
- [ ] Przeglądanie/dodawanie/edycja/usuwanie aukcji.
- [ ] Możliwość składania ofert w ramach aukcji.
- [ ] Automatyczne zakończenie aukcji z powiadomieniem autora i osoby, która wygrała aukcję poprzez e-mail.
- [ ] Uwierzytelnianie.
