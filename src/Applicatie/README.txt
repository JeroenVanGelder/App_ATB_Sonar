Uitleg opdracht 2A:

Instanties van klasse Klus kunnen nu geïnstantieerd worden via de KlusBuilder.
Dit is gedaan door te refactoren.

Voorbeelden van het aanroepen van de Builder zitten in klasse NewKlus op regel 249 en in databaseController op regel 335.

Rick Ossendrijver.

- Change method signature					Auto.java regel 134
- Extract method							Auto.java regel 134
- Extract method object						databaseController.java regel 568
- Extract parameter object					Auto.java regel 77
- Extract constant/field/variable			Auto.java regel 149
- Inline									databaseController regel 55
- Extract interface/abstract pull up/down	InterfacedatabaseController.java
- Remove middleman							Klus.java regel 83 tot 104
- Introduce factory method					Factuur.java regel 29
- Introduce builder							Auto.java regel 68
- Move refactoring							brievenService.java in Package applicatie2
- Replace inheritance with delegation		AP1.java en AP2.java
- Make static/instance						Brandstof.java regel 211