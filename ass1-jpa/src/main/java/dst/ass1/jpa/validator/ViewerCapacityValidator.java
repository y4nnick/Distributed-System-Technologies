package dst.ass1.jpa.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * Created by amra.
 *
 * Checks if viewer cpacity is between min and max.
 */

public class ViewerCapacityValidator implements ConstraintValidator<ViewerCapacity, Integer> {

    private int max;
    private int min;

    @Override
    public void initialize(ViewerCapacity viewerCapacity) {
        min = viewerCapacity.min();
        max = viewerCapacity.max();
    }


    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintContext) {
        return (integer >= min && integer <= max);
    }

}
