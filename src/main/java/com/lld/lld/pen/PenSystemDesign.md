# Design a pen

## Requirements

* A pen is anything that can write.
* Pen can be Gel, Ball, Fountain, Marker.
* Ball Pen and Gel Pen have a Ball Pen Refill and a Gel Pen Refill respectively to write.
* A refil has a tip and an ink.
* Ink can be of different colour
* A fountain pen has an Ink.
* Refil has a radius. 
* For fountain pen, its tip has a radius.
* Each pen can write in a different way.
* Some pens write in the same way.
* Every pen has a brand and a name.
* Some pens may allow refilling while others might not.

## Enteties and Attributes

* Pen
    - penType: (GEL, BALL, FOUNTAIN, USEANDTHROW)
    - refill: Refill
    - ink: Ink
    - nib: Nib
    - brand
    - color
    - name
    - price


```mermaid
    classDiagram
        class Pen {
            - name: string
            - color: string
            - price: double
            - penType: enum
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill): void
            + changeInk(Ink ink): void
        }

        class Refill {
            - refillType: enum
            - nib: Nib
            - ink: Ink
            - radius: double
            - refillable: bool
        }

        class Ink {
            - colour: string
            - inkType: enum
        }

        class Nib {
            - nibType: enum
            - radius: double
        }

        class NibType {
            <<enumeration>>
            SILVER
            GOLD
        }

        class RefillType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        class PenType {
            GEL
            BALL
            FOUNTAIN
            USEANDTHROW
        }

        class InkType {
            GEL
            BALL
            FOUNTAIN
        }


        Pen "1" --* "1" Refill
        Pen "*" --o "1" PenType


        Refill "1" --* "1" Ink
        Refill "1" --* "1" Nib
        Refill "*" --o "1" RefillType

        Ink "*" --o "1" InkType

        Nib "*" --o "1" NibType
```

## Drawbacks
* SRP is violated as Pen class is having all differennt methods.
* OCP is violated as if I add new type of Pen then we need to open then Pen class.
* Object creation is complex.
* Need to handle refill and write method for each type of pen separately.

```mermaid
    classDiagram
        class Pen {
            <<abstract>>
            - name: string
            - colour: string
            - brand: string
            - price: double
            - penType: PenType
            + write()*: void
            + changeRefill(Refill refill)* void
        }

        class Refill {
            - refillType: enum
            - nib: Nib
            - ink: Ink
            - radius: double
            - refillable: bool
        }

        class GelPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class BallPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class FountainPen {
            - ink: Ink;
            - nib: Nib;
            + write(): void
            + changeRefill(Refill refill) void
        }

        class UseAndThrowPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class Ink {
            - colour: string
            - inkType: enum
        }

        class Nib {
            - nibType: enum
            - radius: double
        }

        class NibType {
            <<enumeration>>
            SILVER
            GOLD
        }

        class RefillType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        class PenType {
            GEL
            BALL
            FOUNTAIN
            USEANDTHROW
        }

        class InkType {
            GEL
            BALL
            FOUNTAIN
        }

        Pen "*" --o "1" PenType

        Pen <|-- GelPen
        Pen <|-- BallPen
        Pen <|-- FountainPen
        Pen <|-- UseAndThrowPen

        GelPen "1" --* "1" Refill
        BallPen "1" --* "1" Refill
        UseAndThrowPen "1" --* "1" Refill

        FountainPen "1" --* "1" Ink
        FountainPen "1" --* "1" Nib

        Refill "1" --* "1" Ink
        Refill "1" --* "1" Nib
        Refill "*" --o "1" RefillType

        Ink "*" --o "1" InkType

        Nib "*" --o "1" NibType
```

## Drawbacks
* ~~SRP is violated as Pen class is having all differennt methods.~~
* ~~OCP is violated as if I add new type of Pen then we need to open then Pen class.~~
* Object creation is complex.
* LSP is violated as Fountain pen thorws exception when changeRefill is called since fountain pen does not have refill.
* subClass are used to create class.
* Code duplicacy
* Class Explosion


## Using Abstract class

```mermaid
    classDiagram
        class Pen {
            <<abstract>>
            - name: string
            - brand: string
            - colour: string
            - price: double
            - writingStrategy: WritingStrategy
            + write()*: void
        }

        class WritingStrategy {
            <<interface>>
            + write(): void
        }

        class SmoothWritingStrategy {
            + write(): void
        }

        class RoughWritingStrategy {
            + write(): void
        }

        class NonRefillablePen {
            <<abstract>>
            - nib: Nib
            - ink: Ink
            + changeInk(Ink ink)*: void
        }

        class RefillablePen {
            <<abstract>>
            - refill: Refill
            + changeRefill(Refill refill)* void
        }

        class Refill {
            - refillType: enum
            - nib: Nib
            - ink: Ink
            - radius: double
            - refillable: bool
        }

        class GelPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class BallPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class FountainPen {
            - ink: Ink;
            - nib: Nib;
            + write(): void
            + changeRefill(Refill refill) void
        }

        class UseAndThrowPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class Ink {
            - colour: string
            - inkType: enum
        }

        class Nib {
            - nibType: enum
            - radius: double
        }

        class NibType {
            <<enumeration>>
            SILVER
            GOLD
        }

        class RefillType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        class PenType {
            GEL
            BALL
            FOUNTAIN
            USEANDTHROW
        }

        class InkType {
            GEL
            BALL
            FOUNTAIN
        }

        Pen "*" --o "1" PenType
        Pen <|-- RefillablePen : extends
        Pen <|-- NonRefillablePen : extends
        Pen "*" --o "1" WritingStrategy

        WritingStrategy <|-- SmoothWritingStrategy : extends
        WritingStrategy <|-- RoughWritingStrategy : extends

        RefillablePen <|-- GelPen : extends
        RefillablePen <|-- BallPen : extends
        NonRefillablePen <|-- FountainPen : extends
        Pen <|-- UseAndThrowPen : extends

        RefillablePen "1" --* "1" Refill
        Refill "1" --* "1" Nib
        Refill "1" --* "1" Ink
        Refill "*" --o "1" RefillType

        NonRefillablePen "1" --* "1" Nib
        NonRefillablePen "1" --* "1" Ink

        Ink "*" --o "1" InkType

        Nib "*" --o "1" NibType
        
```

## Drawbacks
* ~~SRP is violated as Pen class is having all differennt methods.~~
* ~~OCP is violated as if I add new type of Pen then we need to open then Pen class.~~
* Object creation is complex.
* ~~LSP is violated as Fountain pen thorws exception when changeRefill is called since fountain pen does not have refill.~~
* subClass are used to create class.
* Code duplicacy
* Class Explosion

## Using Interface

```mermaid
    classDiagram
        class Pen {
            <<abstract>>
            - name: string
            - brand: string
            - colour: string
            - price: double
            - writingStrategy: WritingStrategy
            + write()*: void
        }

        class WritingStrategy {
            <<interface>>
            + write(): void
        }

        class SmoothWritingStrategy {
            + write(): void
        }

        class RoughWritingStrategy {
            + write(): void
        }

        class RefillablePen {
            <<interface>>
            + changeRefill(Refill refill)*: void
        }

        class NonRefillablePen {
            <<interface>>
            + changeInk(Ink ink)*: void
        }

        class Refill {
            - refillType: enum
            - nib: Nib
            - ink: Ink
            - radius: double
            - refillable: bool
        }

        class GelPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class BallPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class FountainPen {
            - ink: Ink
            - nib: Nib
            + write(): void
            + changeInk(Ink ink) void
        }

        class UseAndThrowPen {
            - refill: Refill
            + write(): void
        }

        class Ink {
            - colour: string
            - inkType: enum
        }

        class Nib {
            - nibType: enum
            - radius: double
        }

        class NibType {
            <<enumeration>>
            SILVER
            GOLD
        }

        class RefillType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        class PenType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
            USEANDTHROW
        }

        class InkType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        Pen "*" --o "1" PenType
        Pen "*" --o "1" WritingStrategy

        WritingStrategy <|-- SmoothWritingStrategy
        WritingStrategy <|-- RoughWritingStrategy

        Pen <|-- GelPen
        Pen <|-- BallPen
        Pen <|-- FountainPen
        Pen <|-- UseAndThrowPen

        GelPen ..|> RefillablePen
        BallPen ..|> RefillablePen
        FountainPen ..|> NonRefillablePen

        RefillablePen "1" --* "1" Refill
        Refill "1" --* "1" Nib
        Refill "1" --* "1" Ink
        Refill "*" --o "1" RefillType

        NonRefillablePen "1" --* "1" Nib
        NonRefillablePen "1" --* "1" Ink

        Ink "*" --o "1" InkType
        Nib "*" --o "1" NibType
```


## Drawbacks
* ~~SRP is violated as Pen class is having all differennt methods.~~
* ~~OCP is violated as if I add new type of Pen then we need to open then Pen class.~~
* Object creation is complex.
* ~~LSP is violated as Fountain pen thorws exception when changeRefill is called since fountain pen does not have refill.~~
* subClass are used to create class.
* ~~Code duplicacy~~
* Class Explosion

Using Factory Method along with Builder 

```mermaid
    classDiagram
        class Pen {
            <<abstract>>
            - name: string
            - brand: string
            - colour: string
            - price: double
            - writingStrategy: WritingStrategy
            + write()*: void
        }

        class WritingStrategy {
            <<interface>>
            + write(): void
        }

        class SmoothWritingStrategy {
            + write(): void
        }

        class RoughWritingStrategy {
            + write(): void
        }

        class RefillablePen {
            <<interface>>
            + changeRefill(Refill refill)*: void
        }

        class NonRefillablePen {
            <<interface>>
            + changeInk(Ink ink)*: void
        }

        class Refill {
            - refillType: enum
            - nib: Nib
            - ink: Ink
            - radius: double
            - refillable: bool
        }

        class GelPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class BallPen {
            - refill: Refill
            + write(): void
            + changeRefill(Refill refill) void
        }

        class FountainPen {
            - ink: Ink
            - nib: Nib
            + write(): void
            + changeInk(Ink ink) void
        }

        class UseAndThrowPen {
            - refill: Refill
            + write(): void
        }

        class Ink {
            - colour: string
            - inkType: enum
        }

        class Nib {
            - nibType: enum
            - radius: double
        }

        class AbstractPenFactory {
            <<abstract>>
            + createPen()* Pen
        }

        class GelPenFactory {
            + createPen() Pen
        }

        class BallPenFactory {
            + createPen() Pen
        }

        class FountainPenFactory {
            + createPen() Pen
        }

        class UseAndThrowPenFactory {
            + createPen() Pen
        }

        class NibType {
            <<enumeration>>
            SILVER
            GOLD
        }

        class RefillType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        class PenType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
            USEANDTHROW
        }

        class InkType {
            <<enumeration>>
            GEL
            BALL
            FOUNTAIN
        }

        Pen "*" --o "1" PenType
        Pen "*" --o "1" WritingStrategy

        WritingStrategy <|-- SmoothWritingStrategy
        WritingStrategy <|-- RoughWritingStrategy

        Pen <|-- GelPen
        Pen <|-- BallPen
        Pen <|-- FountainPen
        Pen <|-- UseAndThrowPen

        GelPen ..|> RefillablePen
        BallPen ..|> RefillablePen
        FountainPen ..|> NonRefillablePen

        AbstractPenFactory <|-- GelPenFactory
        AbstractPenFactory <|-- BallPenFactory
        AbstractPenFactory <|-- FountainPenFactory
        AbstractPenFactory <|-- UseAndThrowPenFactory

        GelPen <-- GelPenFactory
        BallPen <-- BallPenFactory
        FountainPen <-- FountainPenFactory
        UseAndThrowPen <-- UseAndThrowPenFactory

        Pen <-- AbstractPenFactory

        RefillablePen "1" --* "1" Refill
        Refill "1" --* "1" Nib
        Refill "1" --* "1" Ink
        Refill "*" --o "1" RefillType

        NonRefillablePen "1" --* "1" Nib
        NonRefillablePen "1" --* "1" Ink

        Ink "*" --o "1" InkType
        Nib "*" --o "1" NibType
```




