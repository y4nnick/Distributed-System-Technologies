package dst.ass1.jpa.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ViewerCapacityValidator.class)

public @interface ViewerCapacity {
    String message() default "Viewer capacity has to be between predefined values";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    int min() default 0;
    int max() default Integer.MAX_VALUE;
}