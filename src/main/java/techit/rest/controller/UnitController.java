package techit.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Unit;
import techit.model.dao.UnitDao;
import techit.rest.error.RestException;

@RestController
public class UnitController {

	@Autowired
	private UnitDao unitDao;

	@RequestMapping(value = "/unit/{id}", method = RequestMethod.GET)
	public Unit getUnit(@PathVariable Long id) {
		return unitDao.getUnit(id);
	}

	@RequestMapping(value = "/unit/", method = RequestMethod.GET)
	public List<Unit> getUnits() {
		return unitDao.getUnits();
	}

	@RequestMapping(value = "/unit/", method = RequestMethod.PUT)
	public Unit updateUnit(@RequestBody Unit unit) {
		if (unit.getId() == null || unit.getName() == null)
			throw new RestException(400, "Missing Unit id and/or name field.");

		return unitDao.saveUnit(unit);
	}

	@RequestMapping(value = "/unit/", method = RequestMethod.POST)
	public Unit addUnit(@RequestBody Unit unit) {
		if (unit.getName() == null)
			throw new RestException(400, "Missing Unit name field.");

		return unitDao.saveUnit(unit);
	}

}