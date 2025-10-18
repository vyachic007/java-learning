package autocompany.assembly;

import autocompany.interfaces.IAssemblyLine;
import autocompany.interfaces.ILineStep;
import autocompany.interfaces.IProduct;
import autocompany.interfaces.IProductPart;

public class CarAssemblyLine implements IAssemblyLine {

    private final ILineStep bodyStep;
    private final ILineStep chassisStep;
    private final ILineStep engineStep;

    public CarAssemblyLine(ILineStep bodyStep, ILineStep chassisStep, ILineStep engineStep) {
        this.bodyStep = bodyStep;
        this.chassisStep = chassisStep;
        this.engineStep = engineStep;
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("<<<< НАЧАЛО СБОРКИ >>>>>\n");

        IProductPart body = bodyStep.buildProductPart();
        product.installFirstPart(body);

        IProductPart chassis = chassisStep.buildProductPart();
        product.installSecondPart(chassis);

        IProductPart engine = engineStep.buildProductPart();
        product.installThirdPart(engine);

        System.out.println("\n<<<< СБОРКА ЗАВЕРШЕНА  >>>>>");

        return product;
    }
}