# Designing Splitwise

## Requirements
1. The system should allow users to create accounts and manage their profile information.
2. Users should be able to create groups and add other users to the groups.
3. Users should be able to add expenses within a group, specifying the amount, description, and participants.
4. The system should automatically split the expenses among the participants based on their share.
5. Users should be able to view their individual balances with other users and settle up the balances.
6. The system should support different split methods, such as equal split, percentage split, and exact amounts.
7. Users should be able to view their transaction history and group expenses.
8. The system should handle concurrent transactions and ensure data consistency.

## Entities and Attributes

* User
* Group
* Expense
* Transaction

## UML Class Diagram

```mermaid
classDiagram
    class SplitWiseSystem {
        - instance: SplitWiseSystem
        - users: Map<String, User>
        - groups: Map<String, Group>
        + getInstance(): SplitWiseSystem
        + createUser(name: String, email: String): User
        + createGroup(name: String, members: List<User>): Group
    }

    class User {
        - name: String
        - email: String
    }

    class Group {
        - name: String
        - members: List<User>
        - balanceSheet: BalanceSheet
        + addUser(user: User): void
        + removeUser(user: User): void
        + addExpense(description: String, paidBy: User, amount: Double, users: List<User>, method: SplitMethod, details: Double[]): void
        + settleBalance(payer: User, receiver: User, amount: Double): void
    }

    class Expense {
        - description: String
        - paidBy: User
        - amount: Double
        - usersToSplit: List<User>
        - splitMethod: SplitMethod
        - distribution: Map<User, Double>
    }

    class BalanceSheet {
        - balance: Map<User, Map<User, Double>>
        - splitStrategy: SplitStrategy
        + updateBalance(paidBy: User, owes: List<User>, amount: Double)
        + showBalance()
        + showUserBalance(user: User)
    }

    class SplitMethod {
        <<enumeration>>
        EQUAL
        PERCENTAGE
        EXACT
    }

    class SplitStrategy {
        <<interface>>
        + split(expense: Expense)
    }

    class EqualSplitStrategy {
        + split(expense: Expense)
    }

    class PercentageSplitStrategy {
        + split(expense: Expense)
    }

    class ExactSplitStrategy {
        + split(expense: Expense)
    }

    class SplitStratgeyFactory {
        + createSplitFactory(SplitMethod): SplitStrategy
    }

    SplitWiseSystem --* User
    SplitWiseSystem --* Group
    Group --* User
    Group --* BalanceSheet
    Group --* Expense
    Expense --o SplitMethod
    BalanceSheet --o SplitStrategy
    SplitStrategy <|.. EqualSplitStrategy
    SplitStrategy <|.. PercentageSplitStrategy
    SplitStrategy <|.. ExactSplitStrategy

    SplitStratgeyFactory --> SplitStrategy

```