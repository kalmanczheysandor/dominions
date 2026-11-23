

> [!NOTE]
> - A végső üzemeltetési tesztek folyamatban vannak még. Várható befejezés 2026.11.23 23:59
> - A teszteléshez kizárólag a lentebb  megadott felhasználói fiókokat használja!

# DOMINIONS

**Készítő:** Kálmánchey Sándor  
**E-mail:** kalmanczheysandor@gmail.com  
**Tel.:** +3620/667-41-48  
**Verzió:** 1.0  
**Állapot:** Majdnem kész

---

**Szakdolgozat:** MESTERSÉGES INTELLIGENCIA ALKALMAZÁSA TÖBBSZEMÉLYES JÁTÉKOK NAGYVÁLLALATI SZEMLÉLETŰ IMPLEMENTÁCIÓJÁBAN  
**Témavezető:** Dr. Kádek Tamás  
**Intézmény:** Debreceni Egyetem – Informatikai Kar

---

## 1. A projektről

Az elkészített alkalmazáscsomag egy olyan szolgáltatást valósít meg, amely egy dedikált online felületen keresztül biztosít játéklehetőséget természetes és mesterséges játékosok számára egyaránt. A saját tervezésű **Dominions** egy többszereplős, determinisztikus, körökre osztott stratégiai játék, amelyben a résztvevők szimultán, egymás döntéseinek ismerete nélkül hozzák meg lépéseiket. A felhasználók különböző játékpályák közül választhatnak, játékmeneteket indíthatnak, mások játékaihoz csatlakozhatnak, és eltérő mesterségesintelligencia-algoritmusokkal megvalósított gépi játékosokkal is megküzdhetnek.

Az alkalmazáscsomag két önálló, **Vue.js-alapú kliensalkalmazást** – egy játékfelületet és egy adminisztrációs felületet –, valamint egy **Spring Cloudra épülő, microservice-alapú, konténerizálható backendrendszert** foglal magában. Az elkülönített adminisztrációs felület lehetőséget biztosít a felhasználók és jogosultságok, a játékpályák, valamint a mesterséges játékosok és konfigurációik kezelésére, továbbá a neurális hálók betanítási folyamatainak vezérlésére és eredményességük ellenőrzésére.

---

## 2. Tesztelési hozzáférések

Az elkészített alkalmazás telepített és üzembe helyezett változata az alábbi webcímeken érhető el.

A rendszer kipróbálásához előre létrehozott tesztfelhasználói fiókok állnak rendelkezésre.

### 2.1. Adminisztrációs felület

**Elérhetőség:** https://admin.dominions.hu

Az adminisztrációs felület a rendszer üzemeltetéséhez és kezeléséhez szükséges funkciókat biztosítja, többek között a felhasználók, a jogosultságok, a játékpályák és a mesterséges játékosok adminisztrációját. Valamint a neurális hálók betanítását.

#### Teszteléshez használható fiókok

| Felhasználónév (e-mail) | Jelszó |
|---|---|
| admin.andor1@game.hu | `AdminAndor1` |
| admin.andor2@game.hu | `AdminAndor2` |
| admin.andor3@game.hu | `AdminAndor3` |
| admin.andor4@game.hu | `AdminAndor4` |

### 2.2. Játékfelület

**Elérhetőség:** https://game.dominions.hu

A játékfelületen a felhasználók bejelentkezhetnek, különböző játékpályák közül választhatnak, új játékmeneteket indíthatnak, illetve csatlakozhatnak mások által létrehozott játékokhoz.

A játékmenetekben természetes és mesterséges játékosok egyaránt részt vehetnek.

#### Teszteléshez használható fiókok

| Felhasználónév (e-mail) | Jelszó |
|---|---|
| jatekos.jakab1@game.hu | `JatekosJakab1` |
| jatekos.jakab2@game.hu | `JatekosJakab2` |
| jatekos.jakab3@game.hu | `JatekosJakab3` |
| jatekos.jakab4@game.hu | `JatekosJakab4` |

---

### 2.3. Megjegyzések a teszteléshez

A felsorolt felhasználói fiókok a szakdolgozati alkalmazás funkcióinak kipróbálására szolgálnak.

A több rendelkezésre álló játékosfiók lehetővé teszi a többszemélyes játékmenetek tesztelését, akár több böngészőablak vagy különböző böngészők használatával.

**Figyelem:** Az alkalmazás végső üzemeltetési ellenőrzései még folyamatban vannak, ezért egyes funkciók működésében átmeneti problémák előfordulhatnak.
