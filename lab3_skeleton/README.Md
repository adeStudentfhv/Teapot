# 3D Software Renderer – Pipes-and-Filters Architektur

**Autoren:** Ali Demir, Niklas Rist  
**Projekt:** Lab 3 – Computer Graphics Pipeline (FHV)

## Ziel

In diesem Projekt wurde eine vollständige 3D-Rendering-Pipeline umgesetzt, die nach dem **Pipes-and-Filters-Architekturmuster** funktioniert. Dabei übernimmt jeder Filter genau eine definierte Teilaufgabe (Transformation, Shading, Culling, Rendering) und gibt seine Ergebnisse über eine definierte Pipe an den nächsten Filter weiter.

## Aufbau

Das System enthält zwei Varianten der Pipeline:

- **Push-Pipeline**: Daten werden vom Anfang (ModelViewTransform) aktiv weitergeschoben.
- **Pull-Pipeline**: Das Rendering wird vom Ende (SinkPullFilter) durch `pull()`-Aufrufe ausgelöst.

Beide Pipelines unterstützen folgende Verarbeitungsschritte:

- Modelltransformation (Model-View)
- Backface Culling
- Depth Sorting *(nur Push)*
- Lighting mit Flat- oder Gouraud-Shading
- Projektion (perspektivisch)
- Screenspace-Transformation
- Rendering als Punkt, Linie, Fläche oder mit Shading

## Architektur

Die Architektur besteht aus modularen Filtern, die jeweils ein generisches Interface implementieren:

- `PushFilter<I>` mit `void push(I input)`
- `PullFilter<T>` mit `T pull()`
- `AbstractPushFilter<I, O>` als Hilfsklasse zur Verkettung

### Modellstruktur

Die Daten stammen aus einem `.obj`-Modell (Utah-Teapot), das über folgende Klassen verarbeitet wird:

- `ObjLoader` – lädt das Modell
- `Model` – enthält alle `Face`-Objekte
- `Face` / `ColoredFace` – Darstellung einzelner Flächen
- `FaceTransformer`, `VectorUtils` – Hilfsklassen für Transformation und Geometrie

## Ausführen

### Gradle (empfohlen)

```bash
./gradlew run
