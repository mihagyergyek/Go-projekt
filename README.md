# Go
Projektna naloga za Programiranje 2

## Uvod
V tem repozitoriju se nahajajo datoteke za igro Go. Datoteke za to igro najdemo v mapi Oddaja ali src. V mapi Tekmovanje najdemo datoteke za igro Capture Go, ki je preprostejša verzija originalne igre.

## Prvi zagon
Za začetek potrebujemo vse datoteke iz mape src (ali Tekmovanje). Igro nato poženemo tako, da zaženemo metodo main v datoteki Go.java (oziroma CaptureGo.java).

## Nadaljna uporaba
Igro zaženemo z metodo main.

## Uporaba
Ko igro prvič odpremo, se pred nami pojavi plošča. Pred začetkom igre v nastavitvah spremenimo velikost plošče, če s to nismo zadovoljni.
Nato v meniju Nova igra izberemo eno od naslednjih možnosti:
- Človek - človek: dva človeška igralca lahko igrata en proti drugemu
- Človek - računalnik: človek igra proti računalniškemu igralcu in igra prvi (kot črni)
- Računalnik - človek: človek igra proti računalniškemu igralcu in igra drugi (kot beli)
- Računalnik - računalnik: računalniška igralca igrata en proti drugemu

Priporočena velikost plošče z računalniškim igralcem je med 9x9 in 15x15. Z izbiro se začne nova igra.

## Druge nastavitve
Kadarkoli med igro lahko spremenimo barvo žetonov, velikost žetonov (vrednost med 0 in 1) in barvo plošče. Spreminjanje velikosti plošče med igro bo trenutno igro prekinilo.
Prav tako lahko med igro trenutno igro shranimo ali odpremo staro igro. To naredimo v meniju Igra. Trenutna igra se avtomatsko shranjuje v trenutna_igra.txt.
Opozorilo: shranjevanje ne shrani prejšnjih pozicij v igri, zato se po odprtju stare igre lahko krši ko pravilo.

## Pravila igre
Igra uporablja običajna pravila [Go](https://en.wikipedia.org/wiki/Rules_of_Go). Med drugim to pomeni, da niso dovoljene poteze, ki bi ujele lastne žetone.
Prav tako je končna pozicija plošče tista, ki ostane po tem, ko oba igralca preskočita potezo. To pomeni, da morata igralca ujeti tudi skupine, za katere oba menita, da niso žive.
Računalniški igralec bo igro igral "do konca". Če ugotovi, da je njegov položaj preslab, bo vsako naslednjo potezo preskočil, dokler se nasprotnik ne odloči končati igre.
Igralec svojo potezo preskoči z gumbom v meniju Poteza ali s pritiskom preslednice, ko je na vrsti.
Točkovanje igre v teritorij igralca šteje tudi žetone, ki jih je igralec odigral. Igra na koncu z zeleno označi polja, ki pripadajo belemu, z rdečo pa polja, ki pripadajo črnemu igralcu.
