## Requirements
1. The food delivery service should allow customers to browse restaurants, view menus, and place orders.
2. Restaurants should be able to manage their menus, prices, and availability.
3. Delivery agents should be able to accept and fulfill orders.
4. The system should handle order tracking and status updates.
5. The system should support multiple payment methods.
6. The system should handle concurrent orders and ensure data consistency.
7. The system should be scalable and handle a high volume of orders.
8. The system should provide real-time notifications to customers, restaurants, and delivery agents.

### Entities and Attributes

* Resturent
    - Menu
    - Location
* Menu
    - dish
* Order
    - resturent
    - agent
    - dish
    - price
* Delivery Agent
* Dish
    - price
    - quantity
* User
    - Location

### Use case Diagram

```plantuml
    @startuml
    left to right direction

    actor Customer
    actor Restaurant
    actor DeliveryAgent
    actor PaymentGateway

    rectangle "Food Delivery System" {

    usecase "Browse Restaurants" as UC1
    usecase "View Menu" as UC2
    usecase "Place Order" as UC3
    usecase "Make Payment" as UC4
    usecase "Track Order" as UC5
    usecase "Receive Notifications" as UC6

    usecase "Manage Menu" as UC7
    usecase "Update Prices" as UC8
    usecase "Update Availability" as UC9
    usecase "Accept / Reject Order" as UC10

    usecase "Accept Delivery" as UC11
    usecase "Pickup Order" as UC12
    usecase "Deliver Order" as UC13
    usecase "Update Order Status" as UC14
    }

    Customer --> UC1
    Customer --> UC2
    Customer --> UC3
    Customer --> UC4
    Customer --> UC5
    Customer --> UC6

    Restaurant --> UC7
    Restaurant --> UC8
    Restaurant --> UC9
    Restaurant --> UC10
    Restaurant --> UC6

    DeliveryAgent --> UC11
    DeliveryAgent --> UC12
    DeliveryAgent --> UC13
    DeliveryAgent --> UC14
    DeliveryAgent --> UC6

    UC3 --> UC4 : <<include>>
    UC11 --> UC14 : <<include>>
    UC12 --> UC14 : <<include>>
    UC13 --> UC14 : <<include>>

    PaymentGateway --> UC4

    @enduml
```

```mermaid
classDiagram
    %% ================= FACADE PATTERN =================
    class FoodDeliverySystem {
        - instance : FoodDeliverySystem$
        - orderService : OrderService
        - resturentSearchService : SearchService~Resturent~
        - menuSearchService : SearchService~Menu~
        - paymentService : PaymentService
        - userRepository : UserRepository
        - resturentRepository : ResturentRepository
        - agentRepository : AgentRepository
        - menuRepository : MenuRepository
        + getInstance()$ FoodDeliverySystem
        + registerUser(User)
        + registerResturent(Resturent)
        + registerAgent(Agent)
        + addMenu(Menu)
        + browseResturents(query) List~Resturent~
        + browseMenus(query) List~Menu~
        + placeOrder(Order, resturentId) Order
        + confirmOrder(orderId)
        + prepareOrder(orderId)
        + orderReadyForPickup(orderId)
        + assignAgent(orderId)
        + startDelivery(orderId)
        + completeDelivery(orderId)
        + cancelOrder(orderId)
        + trackOrder(orderId) Order
        + setPaymentStrategy(PaymentStrategy)
        + processPayment(amount) boolean
    }

    %% ================= SERVICES =================
    class OrderService {
        - orderRepository : OrderRepository
        - userRepository : UserRepository
        - resturentRepository : ResturentRepository
        - agentRepository : AgentRepository
        + placeOrder(Order, resturentId) Order
        + confirmOrder(orderId)
        + prepareOrder(orderId)
        + orderReadyForPickup(orderId)
        + assignAgent(orderId)
        + startDelivery(orderId)
        + completeDelivery(orderId)
        + cancelOrder(orderId)
    }

    class SearchService~T~ {
        - searchStrategy : SearchStrategy~T~
        + setSearchStrategy(SearchStrategy~T~)
        + search(query) List~T~
    }

    class PaymentService {
        - paymentStrategy : PaymentStrategy
        + setPaymentStrategy(PaymentStrategy)
        + processPayment(amount) boolean
    }

    %% ================= REPOSITORIES (Singleton) =================
    class UserRepository {
        + getInstance()$
        + registerUser(User)
        + getUser(userId)
    }
    class ResturentRepository {
        + getInstance()$
        + registerResturent(Resturent)
        + searchResturent(query)
    }
    class AgentRepository {
        + getInstance()$
        + registerAgent(Agent)
        + findAvailableAgent()
    }
    class MenuRepository {
        + getInstance()$
        + addMenu(Menu)
        + searchMenu(query)
    }
    class OrderRepository {
        + getInstance()$
        + createOrder(Order)
        + getOrder(orderId)
    }

    %% ================= OBSERVER PATTERN =================
    class OrderSubject {
        - observers : List~OrderObserver~
        + addObserver(OrderObserver)
        + removeObserver(OrderObserver)
        + notifyObservers()
    }

    class OrderObserver {
        <<interface>>
        + update(Order)
    }

    class Order {
        - orderId : String
        - orderStatus : OrderStatus
        - dishes : List~Dish~
        + updateStatus(OrderStatus)
    }

    OrderSubject <|-- Order
    OrderObserver <|.. User
    OrderObserver <|.. Resturent
    OrderObserver <|.. Agent

    %% ================= STRATEGY PATTERN (SEARCH) =================
    class SearchStrategy~T~ {
        <<interface>>
        + search(query) List~T~
    }

    class SearchByResturent {
        + search(query) List~Resturent~
    }

    class SearchByMenu {
        + search(query) List~Menu~
    }

    SearchService --* SearchStrategy

    SearchStrategy <|.. SearchByResturent
    SearchStrategy <|.. SearchByMenu

    %% ================= STRATEGY PATTERN (PAYMENT) =================
    class PaymentStrategy {
        <<interface>>
        + processPayment(amount) boolean
    }

    class CardPayment {
        + processPayment(amount)
    }
    class UPIPayment {
        + processPayment(amount)
    }
    class CashOnDelivery {
        + processPayment(amount)
    }

    PaymentService --* PaymentStrategy

    PaymentStrategy <|.. CardPayment
    PaymentStrategy <|.. UPIPayment
    PaymentStrategy <|.. CashOnDelivery

    %% ================= MODELS =================
    class User {
        - userId : String
        - name : String
        + update(Order)
    }

    class Resturent {
        - resturentId : String
        - name : String
        - menus : List~Menu~
        + update(Order)
    }

    class Agent {
        - agentId : String
        - name : String
        - available : boolean
        + update(Order)
    }

    class Menu {
        - menuId : String
        - title : String
        - dishes : List~Dish~
    }

    class Dish {
        - dishId : String
        - name : String
        - price : Double
    }

    %% ================= RELATIONSHIPS =================
    FoodDeliverySystem --> OrderService
    FoodDeliverySystem --> SearchService
    FoodDeliverySystem --> PaymentService
    
    OrderService --> OrderRepository
    OrderService --> UserRepository
    OrderService --> ResturentRepository
    OrderService --> AgentRepository

    Resturent --> Menu
    Menu --> Dish
    Order --> Dish
```