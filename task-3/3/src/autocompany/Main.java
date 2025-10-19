package autocompany;

import autocompany.assembly.CarAssemblyLine;
import autocompany.interfaces.IAssemblyLine;
import autocompany.interfaces.ILineStep;
import autocompany.interfaces.IProduct;
import autocompany.products.Car;
import autocompany.steps.BodyStep;
import autocompany.steps.ChassisStep;
import autocompany.steps.EngineStep;

public class Main {
    public static void main(String[] args) {
        ILineStep bodyStep = new BodyStep();
        ILineStep chassisStep = new ChassisStep();
        ILineStep engineStep = new EngineStep();

        IAssemblyLine carAssemblyLine = new CarAssemblyLine(bodyStep, chassisStep, engineStep);

        IProduct car = new Car();

        car = carAssemblyLine.assembleProduct(car);
        System.out.println(car);
    }
}
