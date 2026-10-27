# MiniBank

Author: Dhairy Chauhan (chauhand2463)

MiniBank is a console banking application developed incrementally across Practical 1–13 of the
Java OOP lab. Each practical adds one layer to the same project: starting from an empty menu
shell and ending with a generic, annotation-driven repository backed by a complete model,
validation, persistence, reporting and concurrency stack.

## Build and run

```bash
javac model/*.java model/annotation/*.java util/*.java service/*.java exception/*.java
jar cfm MiniBank.jar manifest.mf model util service exception
java -jar MiniBank.jar
```

`manifest.mf` declares `Main-Class: service.MiniBank`, so the final artifact is a runnable JAR.

## Project layout

```
MiniBank/
├── model/                      Domain classes and contracts
│   ├── Account.java            Abstract account (state, deposit/withdraw/transfer)
│   ├── SavingsAccount.java     Minimum-balance account, 4% interest
│   ├── CurrentAccount.java     Overdraft account, 0% interest
│   ├── FixedDepositAccount.java Locked deposit, 7% interest, Premium marker
│   ├── Customer.java           Customer with static id generation, Address, clone
│   ├── BankInfo.java           Bank header record
│   ├── Command.java            Parsed transaction command record
│   ├── MenuOption.java         Menu enum
│   ├── TransactionType.java    DEPOSIT / WITHDRAW / TRANSFER
│   ├── Transactable.java       deposit/withdraw interface
│   ├── InterestBearing.java    interestRate + default yearlyInterest
│   ├── WithdrawRule.java       Functional withdrawal rule
│   ├── Premium.java            Marker interface
│   └── annotation/             @Id, @Positive, @MaxLength
├── util/                       Cross-cutting helpers
│   ├── Validator.java          Regex mobile/email/PAN/IFSC validation
│   ├── CommandParser.java      "DEPOSIT AC0001 500" parser
│   ├── StatementFormatter.java StringBuilder statement builder
│   ├── AnnotationValidator.java Reflection-based annotation validation
│   ├── TransactionLogger.java  AutoCloseable audit logger
│   ├── StatePersister.java     Object stream save/load
│   ├── TransactionLog.java     Append-only log writer
│   └── ReportGenerator.java    NIO log walk + end-of-day report
├── service/                    Application and processing layer
│   ├── MiniBank.java           Entry point and demonstration flow
│   ├── AccountWorker.java      Runnable deposit worker (race demo)
│   ├── AccountStore.java       HashMap store + history + sorting
│   ├── Repository.java         Generic @Id-driven repository
│   ├── TransactionProcessor.java Fixed thread pool + ConcurrentHashMap
│   ├── TransactionTask.java    Runnable deposit/withdraw task
│   ├── ProducerConsumer.java   wait/notify ring buffer
│   └── DeadlockDemo.java       Unsafe vs ordered-lock transfers
├── exception/                  Checked banking exceptions
│   ├── BankException.java
│   ├── InsufficientFundsException.java
│   ├── AccountNotFoundException.java
│   └── InvalidAmountException.java
├── lab09-reflection-annotations/  Standalone reflection exercises
├── manifest.mf
└── MiniBank.jar
```

Runtime artifacts are written under `data/` (`accounts.dat`, `logs/*.log`, `report.txt`).

## Practical-wise changes

| Practical | Focus | Main changes |
|-----------|-------|--------------|
| 1 | Menu shell | `MiniBank` with `main`, `BankInfo` record, `MenuOption` enum, Scanner-driven loop until EXIT |
| 2 | Domain model | `Customer` (encapsulation, static `customerCounter`, generated `CUST101` ids), `Account` (generated `AC0001` ids, overloaded constructors, `deposit`/`withdraw`, no balance setter) |
| 3 | Object contracts | `Account.toString`, `equals`/`hashCode` by account number, `Customer.Address` nested class, `Customer.clone` via `Cloneable`, `instanceof` demo |
| 4 | Validation and parsing | Compiled-regex `Validator` (mobile, email, PAN, IFSC), `TransactionType`, `Command` record, `CommandParser`, `StatementFormatter` with `StringBuilder` |
| 5 | Account types | `Account` becomes abstract with `interestRate()` and `canWithdraw(long)`; `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`; polymorphic array loop |
| 6 | Architecture | `Transactable`, `InterestBearing`, `WithdrawRule` (anonymous class + lambda), `Premium`; packages `model`, `util`, `service`; static import; runnable JAR with manifest. Also adds the `lab09-reflection-annotations` exercises |
| 7 | Metadata | `@Id`, `@Positive`, `@MaxLength` in `model.annotation`; annotations on `Account`; reflection-based `AnnotationValidator` returning `String[]` |
| 8 | Error handling | Checked `BankException` hierarchy, `shortfall` in `InsufficientFundsException`; `deposit`/`withdraw` throw instead of returning flags; `transfer` with try/catch/finally; try-with-resources `TransactionLogger` |
| 9 | Threads | `AccountWorker` Runnable; 10 threads racing on one account; `synchronized` deposit/withdraw make the final balance correct; thread lifecycle logging |
| 10 | Transaction engine | `TransactionProcessor` (fixed pool of 4, shutdown/awaitTermination), `TransactionTask`, `ProducerConsumer` with `wait`/`notifyAll`, `DeadlockDemo` with consistent account-number lock ordering |
| 11 | Persistence and reporting | `Serializable` model with `serialVersionUID`; `StatePersister` (Object streams), `TransactionLog` (CREATE/APPEND), `ReportGenerator` (DirectoryStream, BufferedReader, file attributes, summary report) |
| 12 | Collections | `AccountStore` (`HashMap`, `ArrayList` history, Comparator-based `sortedByBalance`), `Account implements Comparable`, `ConcurrentHashMap` shared accounts in `TransactionProcessor` |
| 13 | Generic repository | `Repository<T>` keyed by reflective `@Id` lookup, `findById`/`findAll`/`delete`, `@SafeVarargs saveAll`, bounded `average(N extends Number[])`; final runnable JAR |

## Demonstration flow

`service.MiniBank.main` runs a scripted demonstration before the interactive menu:

1. Creates savings, current and fixed-deposit accounts and performs transactions.
2. Shows polymorphic interest, withdrawal rules, equality, cloning and validators.
3. Parses a sample command and prints a formatted statement.
4. Demonstrates the exception hierarchy, transfer and try-with-resources logging.
5. Runs 10 concurrent workers, a thread-pool batch and a producer/consumer buffer.
6. Saves/reloads accounts, appends transaction logs and prints the end-of-day report.
7. Prints accounts sorted by balance, a store lookup and a generic repository snapshot.

The interactive menu then accepts a number (1–5) and exits on 5.

The deadlock comparison is run separately:

```bash
java -cp . service.DeadlockDemo
```

The reflection exercises in `lab09-reflection-annotations` are compiled and run independently:

```bash
cd lab09-reflection-annotations
javac *.java
java FormValidatorMain
java MiniTestRunner
java RowMapperMain
```
