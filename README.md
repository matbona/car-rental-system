# Car Rental OOP Starter

This is an intentionally small, OOP-focused simulation of a car rental reservation system.

The task statement says that:
- a production-ready solution is not expected,
- around two hours of effort should be enough,
- the solution should focus on non-trivial aspects,
- boilerplate and infrastructure should be avoided,
- depth is preferred over breadth.

For that reason this project does **not** include Spring Boot, REST API, persistence, database setup, authentication or UI.
The solution focuses on the core domain problem: reserving a limited number of cars of a given type for a selected time period.

## Requirements covered

- There are three car types:
  - `SEDAN`
  - `SUV`
  - `VAN`
- The number of cars of each type is limited.
- A customer can reserve a car type for a selected date/time and number of days.
- A reservation is rejected if all cars of the requested type are already reserved in an overlapping period.
- Unit tests verify the core business rules.

## Design focus

I intentionally invested most effort in:

- modelling a reservation period as a domain object,
- correctly detecting overlapping time periods,
- treating reservations as half-open intervals: `[start, end)`,
- enforcing fleet limits per car type,
- keeping the public API of the domain service small,
- covering important edge cases with unit tests.

I intentionally did not spend effort on:

- REST controllers,
- database persistence,
- ORM mappings,
- authentication,
- UI,
- payment flow,
- deployment or production infrastructure.

## Time interval decision

Reservation periods are treated as half-open intervals:

```text
[start, end)
```

This means that a reservation ending at `10:00` does not block another reservation starting exactly at `10:00`.

Example:

```text
Reservation A: 2026-08-01 10:00 -> 2026-08-03 10:00
Reservation B: 2026-08-03 10:00 -> 2026-08-04 10:00
```

These two reservations do not overlap.

## Run tests

```bash
mvn test
```

## Main classes

```text
src/main/java/com/example/carrental
  CarType.java
  ReservationPeriod.java
  Reservation.java
  CarRentalService.java
  CarTypeNotAvailableException.java
```

## Example usage

```java
Map<CarType, Integer> fleet = Map.of(
        CarType.SEDAN, 2,
        CarType.SUV, 1,
        CarType.VAN, 1
);

CarRentalService service = new CarRentalService(fleet);

Reservation reservation = service.reserve(
        CarType.SEDAN,
        LocalDateTime.of(2026, 8, 1, 10, 0),
        3
);
```

## Possible production improvements

If this were evolved into a production-ready solution, I would consider:

- persistent storage,
- transactional consistency on the database level,
- customer identity,
- assigning a concrete car at pickup time,
- REST API or messaging interface,
- idempotency for reservation requests,
- timezone strategy,
- audit log,
- monitoring,
- concurrency tests against the target database.
