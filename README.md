# Progetto_IA NoiRestiamoAChesa(ni)

Prima di tutto è necessario attivare il server a cui si collegherà il player.

Per attivare il player da file jar, è necessario eseguire il seguente comando nella cartella **RunnableJar** del progetto su un prompt dei comandi:
```
java -jar "NoiRestiamoAChesa(ni).jar" <role> <time> <ip>
<role> va sostituito con "white" o "black" (non è case sensitive) ed indica il ruolo del player
<time> va sostituito con un intero positivo che indica il tempo massimo di scelta per il player
<ip> va sostituito con l'indirizzo ip della macchina sulla quale si trova il server
```

Invece per attivare il player dalla **Virtual Machine** fornita è sufficiente apripre un prompt dei comandi ed eseguire i seguenti comandi:
```
cd /tablut
./runmyplayer <role> <time> <ip>
<role> va sostituito con "white" o "black" (non è case sensitive) ed indica il ruolo del player
<time> va sostituito con un intero positivo che indica il tempo massimo di scelta per il player
<ip> va sostituito con l'indirizzo ip della macchina sulla quale si trova il server
```
