package com.lld.lld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lld.lld.pen.enums.InkType;
import com.lld.lld.pen.enums.NibType;
import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.enums.RefillType;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.models.Refill;
import com.lld.lld.pen.interfaces.Pen;
import com.lld.lld.pen.factory.*;

@SpringBootApplication
public class LldApplication {

	public static void main(String[] args) {
		SpringApplication.run(LldApplication.class, args);
		// Ink ink = new Ink("blue", InkType.BALL);
		// Nib nib = new Nib(NibType.SILVER, 0.5);
		// Refill refill = new Refill(RefillType.BALL, ink, nib, 0.5, true);
		// Pen ballpen = new Pen("Link Glasier", "blue", 5.00, "Link", refill, PenType.BALL);

        // Refill newRefill = new Refill(RefillType.BALL, ink, nib, 0.6, true);
        // ballpen.changeRefill(newRefill);

        // ballpen.write();

		Pen gelpen = SimplePenFactory.createPen(PenType.GEL);
		gelpen.write();
	}

}
