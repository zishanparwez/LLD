## Requirements

Design a calendar Application (similar like Google Calendar)
* Ability to create, update, delete an Event
* An event would typically consist of {start, end, location, Owner, user-list, title}.
* Events can either be like meetings(with a dedicated location and appropriate guest-list) or as well be like holidays, birthdays, reminders etc.
* An event once created, can be either accepted or rejected by the constituent users - if neither it should be in neutral state.
* Get Calendar for a user Ui
* Get Event details.
* For a given set of users[U1, U2,....Un] identity a common free slot of time.


## Entites and Attributes

- User
- Event
- Calendar

```mermaid
    classDiagram
    class User {
        - name
        + respond()
    }

    class Event {
        - eventType: EventType
        - startTime
        - endTime
        - location
        - owner: User
        - participants: User[]
        - rsvpStatus: Map~User, RSVPStatus~
        - title
    }

    class EventType {
        <<enumeration>>
        MEETING
        HOLIDAY
        BIRTHDAY
        REMINDER
    }

    class RSVPStatus {
        <<enumeration>>
        ACCEPTED
        REJECTED
        NEUTRAL
    }

    class EventService {
        - eventRepository: EventRepository
        + addEvent(Event)
        + removeEvent(Event)
        + editEvent(Event)
    }

    class CalendarSystem {
        - eventService: EventService
        - availabilityService: AvailabilityService
    }

    class AvailabilityService {
        + findCommonFreeSlots(users, from, to, duration)
    }

    class EventObserver {
        <<interface>>
        + update(Event)
    }

    class EventSubject {
        - observers: EventObserver[]
        + addObserver(EventObserver)
        + notifyObservers(Event)
    }

    class EventRepository {
        - events: Event[]
    }

    class RSVPService {
        <<interface>>
        + respond(Event, User, RSVPStatus)
    }

    User --> RSVPService
    Event --> User
    Event --> EventType
    Event --> RSVPStatus

    User ..|> EventObserver

    Event --|> EventSubject

    EventService --> EventRepository
    EventRepository --> Event
    CalendarSystem --> EventService
    CalendarSystem --> AvailabilityService

```