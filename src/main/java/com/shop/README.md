# ShopProject

### 1. Business description

- Online electronics shop selling computers, smartphones, electronics and accessories.
- A customer browses products and adds them to a cart (computers and smartphones can be configured, e.g. processor, RAM, color).
- A customer places an order and receives an invoice with the net amount, 23% VAT and the gross amount.
- The shop can turn discounts on: 10% for orders from 5000 PLN and 20% from 10000 PLN (off by default).
- Orders can be processed one by one or in parallel, and the stock decreases after each order.
- The order date is stored with a time zone (Europe/Warsaw).

### 2. Technical aspects

- Language and build: Java 17, Maven.
- Libraries: Lombok (less boilerplate code), JUnit 5 and Mockito (tests).
- Layers (packages): `model` (data classes), `service` (business logic), `repository` (storage), `cli` (console menu), `exceptions`.
- Storage is in memory (no database), the repositories are safe to use from many threads.
- Money is kept in `BigDecimal`, rounded to 2 decimals (HALF_UP).
- Orders can be processed synchronously or asynchronously (thread pool, `CompletableFuture`).
- Run the tests: `mvn clean verify` (use Java 17, e.g. `export JAVA_HOME=$(/usr/libexec/java_home -v 17)` on macOS).
- Run the shop: start the `com.shop.Main` class.

### 3. Requirements

- Java 17
- Maven 3.9 or newer

### 4. How to run

- Clone the repository: `git clone https://github.com/stachu28/ShopProject.git`
- Go to the project folder: `cd ShopProject`
- Run the shop: start the `com.shop.Main` class (e.g. in IntelliJ), then choose options from the menu.

### 5. Tests

- Run all tests: `mvn clean verify`
- Tests use JUnit 5 and Mockito.
- Note: the sync vs async performance test takes about 20 seconds.