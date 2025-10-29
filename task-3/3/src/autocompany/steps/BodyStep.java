package autocompany.steps;

import autocompany.interfaces.ILineStep;
import autocompany.interfaces.IProductPart;
import autocompany.parts.Body;

public class BodyStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println(">> Изготовка кузова");
        return new Body();
    }
}
