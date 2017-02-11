/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerctrl.dp;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import fingerctrl.dp.MainTable;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.processing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author JuanManuel
 */
public class EnrollmentForm extends CaptureForm {
    
	private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
	
	EnrollmentForm( Frame owner) {
		super(owner);
                
	}
	
	@Override protected void init()
	{
		super.init();
		this.setTitle("Registro de huella");
		updateStatus();
	}

	@Override protected void process(DPFPSample sample) {
		super.process(sample);
		// Process the sample and create a feature set for the enrollment purpose.
		DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

		// Check quality of the sample and add to enroller if it's good
		if (features != null) try
		{
			makeReport("La muestra se añadió a la plantilla.");
			enroller.addFeatures(features);		// Add feature set to template.
		}
		catch (DPFPImageQualityException ex) { }
		finally {
			updateStatus();

			// Check if template has been created.
			switch(enroller.getTemplateStatus())
			{
				case TEMPLATE_STATUS_READY:	// report success and stop capturing
					stop();
					//((MainFrame)getOwner()).setTemplate(enroller.getTemplate());
                                        
                                        MainTable.getInstance().setTemplate(enroller.getTemplate());// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                        
					setPrompt("Presiona Cerrar y luego presiona Verificar huella.");
					break;

				case TEMPLATE_STATUS_FAILED:	// report failure and restart capturing
					enroller.clear();
					stop();
					updateStatus();
					//((MainFrame)getOwner()).setTemplate(null);
                                        
                                        MainTable.getInstance().setTemplate(null); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                        
					JOptionPane.showMessageDialog(EnrollmentForm.this, "La plantilla de la huella no es valida. Repita el registro de huella.", "Registro de huella", JOptionPane.ERROR_MESSAGE);
					start();
					break;
			}
		}
	}
	
	private void updateStatus()
	{
		// Show number of samples needed.
		setStatus(String.format("Muestras de huella necesarias: %1$s", enroller.getFeaturesNeeded()));
	}
	
}
