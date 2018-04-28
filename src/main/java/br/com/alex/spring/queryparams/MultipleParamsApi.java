package br.com.alex.spring.queryparams;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Tuple2;
import static io.vavr.Predicates.isNotNull;
import static io.vavr.Predicates.isNull;

import io.vavr.Tuple;
import java.util.function.Supplier;
import javax.ws.rs.QueryParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultipleParamsApi {

  @GetMapping("/oop")
  public ResponseEntity oop(
          @QueryParam("one") String one,
          @QueryParam("two") String two) {
    Supplier<String> handler = HandleFactory.createHandler(one, two);
    return ResponseEntity.ok(handler.get());
  }

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

  private String handle(String one, String two) {
    return "two";
  }

  private String handle(String one) {
    return "one";
  }

  private String handle() {
    return "none";
  }

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
