package autocompany.steps;

import autocompany.interfaces.ILineStep;
import autocompany.interfaces.IProductPart;
import autocompany.parts.Engine;

public class EngineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println(">> Изготовка двигателя");
        return new Engine();
    }
}
