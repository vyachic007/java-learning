package autocompany.steps;

import autocompany.interfaces.ILineStep;
import autocompany.interfaces.IProductPart;
import autocompany.parts.Chassis;

public class ChassisStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println(">> Изготовка шасси");
        return new Chassis();
    }

}
