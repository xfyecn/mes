package com.qcadoo.mes.plugins.products.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.qcadoo.mes.core.data.api.DataAccessService;
import com.qcadoo.mes.core.data.api.DataDefinitionService;
import com.qcadoo.mes.core.data.beans.Entity;
import com.qcadoo.mes.core.data.definition.DataDefinition;
import com.qcadoo.mes.core.data.definition.FieldDefinition;

@Controller
public class ProductsModifyController {

	private DataDefinitionService dataDefinitionService;

	private DataAccessService dataAccessService;

	private Logger logger = LoggerFactory
			.getLogger(ProductsModifyController.class);

	@Autowired
	public ProductsModifyController(
			DataDefinitionService dataDefinitionService,
			DataAccessService dataAccessService) {
		this.dataAccessService = dataAccessService;
		this.dataDefinitionService = dataDefinitionService;
		logger.info("constructor - " + dataDefinitionService);
	}

	@RequestMapping(value = "/products/addModifyEntityForm", method = RequestMethod.GET)
	public ModelAndView addModifyEntityForm(
			@RequestParam(required = false) String entityId) {
		try {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("addModifyEntity");

			DataDefinition dataDefinition = dataDefinitionService
					.get("product");
			List<FieldDefinition> fieldsDefinition = dataDefinition.getFields();
			mav.addObject("fieldsDefinition", fieldsDefinition);
			if (entityId != null && !entityId.equals("")) {
				mav.addObject("entityId", entityId);
				Entity entity = dataAccessService.get("product",
						Long.parseLong(entityId));
				mav.addObject("entity", entity.getFields());
			}

			return mav;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e);
		}

	}

	@RequestMapping(value = "/products/addModifyEntity", method = RequestMethod.POST)
	public ModelAndView addModifyEntity(@ModelAttribute Entity entity) {

		DataDefinition dataDefinition = dataDefinitionService.get("product");
		List<FieldDefinition> fieldsDefinition = dataDefinition.getFields();
		ModelAndView mav = new ModelAndView();
		if (checkFields(entity, fieldsDefinition, mav)) {
			String message = "";
			dataAccessService.save("product", entity);
			if (entity.getId() == null) {
				message = "Dodano";
			} else {
				message = "Zmodyfikowano";
			}
			return new ModelAndView("redirect:list.html?message=" + message);
		} else {

			mav.setViewName("addModifyEntity");
			mav.addObject("message", "fullFillFields");
			mav.addObject("entity", entity.getFields());
			mav.addObject("fieldsDefinition", fieldsDefinition);
			mav.addObject("entityId", entity.getId());
			return mav;
		}

	}

	public boolean checkFields(Entity entity,
			List<FieldDefinition> fieldsDefinition, ModelAndView mav) {

		boolean result = true;
		Map<String, String> fieldsValidationInfo = new HashMap<String, String>();
		for (FieldDefinition field : fieldsDefinition) {
			String formField = (String) entity.getField(field.getName());
			if (formField == null || formField.equals("")) {

				String fieldValidationInfo = "requiredField";
				fieldsValidationInfo.put(field.getName(), fieldValidationInfo);
				result = false;
			}

		}
		mav.addObject("fieldsValidationInfo", fieldsValidationInfo);

		return result;
	}

}