package fr.dauphine.spring.ctl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.dauphine.spring.bo.Client;
import fr.dauphine.spring.bo.Commande;
import fr.dauphine.spring.bo.Panier;
import fr.dauphine.spring.dao.impl.ClientDAO;
import fr.dauphine.spring.dao.impl.CommandeDAO;
import fr.dauphine.spring.editor.ClientEditor;

@Controller
@RequestMapping("detailCommande")
public class DetailCommandeController {

	@Autowired
	private Panier panier;

	@Autowired
	private CommandeDAO comDAO;
	
	@Autowired
	private ClientDAO cDAO;
	
	@RequestMapping(method=RequestMethod.GET)
	public String affichage(ModelMap model){
		
		Commande commande = new Commande(panier);
		comDAO.save(commande);
		model.addAttribute("panier", panier);
		model.addAttribute("commande", commande);
		model.addAttribute("produits", IndexController.getProduits());
		return "./Front-Office/detailCommande";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String validation(@ModelAttribute("commande") Commande commande, BindingResult result) throws Exception {
		commande.setDateValidation(new Date());
		// Essai bidon pour l'ajout du client
		commande.setClient(cDAO.get().get(0));
		System.out.println("Ajout du client � la commande!");
		// TODO : Pourquoi le champ Client (VARCHAR[255]) reste alors que la jointure marche?
		comDAO.save(commande);		
		return "redirect:./indexController.action";
    }

	@InitBinder
	protected void initBinderClientId(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Client.class, "client", new ClientEditor(cDAO));
    }
}