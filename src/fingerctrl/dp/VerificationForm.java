/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerctrl.dp;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.Frame;

/**
 *
 * @author JuanManuel
 */
public class VerificationForm extends CaptureForm {
    private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
	
	VerificationForm(Frame owner) {
		super(owner);
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Verificación de huella");
		updateStatus(0);
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);

		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
                
                System.out.println(sample.serialize());
                

		// Check quality of the sample and start verification if it's good
		if (features != null)
		{
			// Compare the feature set with our template
			DPFPVerificationResult result = 
				//verificator.verify(features, ((MainFrame)getOwner()).getTemplate());
                        
                        verificator.verify(features, MainTable.getInstance().getTemplate());
                        
			updateStatus(result.getFalseAcceptRate());
			if (result.isVerified())
				makeReport("Huella dactilar CONFIRMADA");
			else
				makeReport("Huella dactilar NO CONFIRMADA");
                        
		}
	}
	
	private void updateStatus(int FAR)
	{
		// Show "False accept rate" value
		setStatus(String.format("Tasa de aceptación (FAR) = %1$s", FAR));
	}
    
}
