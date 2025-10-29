package autocompany.products;

import autocompany.interfaces.IProduct;
import autocompany.interfaces.IProductPart;

public class Car implements IProduct {

    private IProductPart body;
    private IProductPart chassis;
    private IProductPart engine;

    @Override
    public void installFirstPart(IProductPart part) {
        this.body = part;
        System.out.println("Установлен: " + part);
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.chassis = part;
        System.out.println("Установлен: " + part);
    }

    @Override
    public void installThirdPart(IProductPart part) {
        this.engine = part;
        System.out.println("Установлен: " + part);
    }

    @Override
    public String toString() {
        return String.format("Автомобиль собран из %s, %s, %s", body, chassis, engine);
    }
}
