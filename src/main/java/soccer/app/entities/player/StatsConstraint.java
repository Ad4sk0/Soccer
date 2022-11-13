package soccer.app.entities.player;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Min(1)
@Max(99)
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
public @interface StatsConstraint {

    String message() default "Value should be in range from 1 to 99";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
