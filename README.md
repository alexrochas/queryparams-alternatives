# Query Params

> Minor vavr POC to show how a functional approach can simplify things

### Problem

An endpoint that receives two or more query parameters like:

```
GET /api/books?title=Akira&edition=3
```

### Traditional approach

```java
@GetMapping("/if")
public ResponseEntity ifElse(
        @QueryParam("one") String one,
        @QueryParam("two") String two) {
  if (one != null && two != null) {
    return ResponseEntity.ok(handle(one, two));
  } else if (one != null && two == null) {
    return ResponseEntity.ok(handle(one));
  } else if (one == null && two != null) {
    return ResponseEntity.ok(handle(two));
  }
  return ResponseEntity.ok(handle());
}
```

Ugly and not exactly scalable. We could use Optionals to "hide" those null's but will still be ugly.

### Some O.O.P. approach

```java
@GetMapping("/oop")
public ResponseEntity oop(
        @QueryParam("one") String one,
        @QueryParam("two") String two) {
  Supplier<String> handler = HandleFactory.createHandler(one, two);
  return ResponseEntity.ok(handler.get());
}

class HandleFactory {
  public static Supplier<String> createHandler(String one, String two) {
    if (one != null && two != null) {
      return () -> "two";
    } else if (one != null && two == null) {
      return () -> "one";
    } else if (one == null && two != null) {
      return () -> "one";
    }
    return () -> "none";
  }
}
```

Still very ugly, at least form the one guy that first typed this code. You can hide the nulls wherever you want, but they will exist.

### Functional approach

```java
@GetMapping("/patternMatching")
public ResponseEntity patternMatching(
        @QueryParam("one") String one,
        @QueryParam("two") String two) {
  return ResponseEntity.ok(Match(
          Tuple.of(one, two))
          .of(
                  Case($Tuple2($(isNotNull()), $(isNotNull())), (a, b) -> handle(a, b)),
                  Case($Tuple2($(isNull()), $(isNotNull())), (a, b) -> handle(b)),
                  Case($Tuple2($(isNotNull()), $(isNull())), (a, b) -> handle(a)),
                  Case($(), handle())
          )
  );
}
```

Ok, it hurts at first. Stolen from Scala, given by [VAVR](http://www.vavr.io/). Once you understand how pattern matching works and how you should use the lib things will look easier.



**The purpose of this repository is not teach functional programming, just show that we have alternatives to our ugly and verbose Java.**

## Meta

Alex Rocha - [about.me](http://about.me/alex.rochas)

