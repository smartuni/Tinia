# Tinia

### Inhalt des Projekts
Wenn es draußen stürmt oder uns der Wind langsam durchs Haar streicht, dann hat nur einer die Finger im Spiel: Der Gott Tinia. Was er scheinbar zufällig geschehen lässt, mal launig, mal sanft, wollen wir in Zahlen fassen. Wir fragen uns: Sind Götter messbar? Das RIOT-Projekt Tinia ist die Antwort auf eine geschlossene wissenschaftlich Lücke, die wir wieder öffnen wollen - ähnlich einer ägyptischen Pyramide, in der lange nach der erstmaligen Erforschung eine neue Schatzkammer entdeckt wurde. Ein Annäherungsversuch an eine göttliche Macht, die schwer zu greifen ist. Wir wollen einen Windmesser platzieren und die Werte über das TheThingsNetwork in die Cayenne Cloud leiten, wo sie graphisch aufbereitet werden. Gemeinsam mit dem mächtigen Betriebssystem RIOT entsteht so ein Experiment am Rande des Wahnsinns - im transzendenten Raum zwischen Himmel und Erde.

### Anforderungen an Payload der Sensordaten

Die Daten müssen im LPP-Format gesendet werden.
Hiefür müssen die Daten pro Sensor wiefolgt strukturiert werden:
- Im ersten Byte steht der Channel
- Im zweiten Byte steht der Typ
- Im dritten und vierten Byte stehen die Messwerte

Es können maximal 4 Sensorwerte in einem Payload verschickt werden

**Channel**
- 01 = Durchschnittliche Windgeschwindigkeit
- 02 = Maximale Windgeschwindigkeit
- 03 = Windrichtung
- 04 = Niederschlag

**Typ**

Der Typ muss analog_out (Wert = 03) sein.

**Beispiel**

Für das Senden von durchschnittlichen Windgeschwindigkeitsdaten:

```
01 03 02 00
```


