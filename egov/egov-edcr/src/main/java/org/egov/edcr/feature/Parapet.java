/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2019>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.edcr.feature;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.springframework.stereotype.Service;

@Service
public class Parapet extends FeatureProcess {

	private static final Logger LOG = Logger.getLogger(Parapet.class);
	private static final String RULE_41_V = "41-v";
	public static final String PARAPET_DESCRIPTION = "Parapet";

	private static final int COLOR_GENRAL_STAIR_CASE_RAILLING = 1;
	private static final int COLOR_DARAMP_RAILLING = 2;
	private static final int COLOR_PARAPET = 3;

	@Override
	public Plan validate(Plan pl) {

		return pl;
	}

	@Override
	public Plan process(Plan pl) {
		prepareParapet(pl);
		validateGenrailStairParapet(pl);
		validateDaRamParapet(pl);
		validateParapet(pl);
		return pl;
	}
	
	private void validateParapet(Plan pl) {

		ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.setKey("Common_Parapet");
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, DESCRIPTION);
		scrutinyDetail.addColumnHeading(3, REQUIRED);
		scrutinyDetail.addColumnHeading(4, PROVIDED);
		scrutinyDetail.addColumnHeading(5, STATUS);
		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, RULE_41_V);
		details.put(DESCRIPTION, PARAPET_DESCRIPTION);

		BigDecimal minHeight = BigDecimal.ZERO;

		for (Block b : pl.getBlocks()) {
			if (b.getGenralParapets() != null && !b.getGenralParapets().isEmpty()) {
				minHeight = b.getGenralParapets().stream().reduce(BigDecimal::min).get();

				if (minHeight.compareTo(new BigDecimal("1")) >= 0) {

					details.put(REQUIRED, "Height >= 1");
					details.put(PROVIDED, "Height >= " + minHeight);
					details.put(STATUS, Result.Accepted.getResultVal());
					scrutinyDetail.getDetail().add(details);
					pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);

				} else {
					details.put(REQUIRED, "Height >= 1");
					details.put(PROVIDED, "Height >= " + minHeight);
					details.put(STATUS, Result.Not_Accepted.getResultVal());
					scrutinyDetail.getDetail().add(details);
					pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
				}
			}
		}
	
	}

	private void validateDaRamParapet(Plan pl) {
		ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.setKey("Common_DA Ramp Railing");
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, DESCRIPTION);
		scrutinyDetail.addColumnHeading(3, REQUIRED);
		scrutinyDetail.addColumnHeading(4, PROVIDED);
		scrutinyDetail.addColumnHeading(5, STATUS);
		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, RULE_41_V);
		details.put(DESCRIPTION, "DA Ramp Railing");

		BigDecimal minHeight = BigDecimal.ZERO;

		for (Block b : pl.getBlocks()) {
			if (b.getdARailingParapets() != null && !b.getdARailingParapets().isEmpty()) {
				minHeight = b.getdARailingParapets().stream().reduce(BigDecimal::min).get();

				if (minHeight.compareTo(new BigDecimal("0.8")) == 0) {

					details.put(REQUIRED, "Height = 0.8");
					details.put(PROVIDED, "Height = " + minHeight);
					details.put(STATUS, Result.Accepted.getResultVal());
					scrutinyDetail.getDetail().add(details);
					pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);

				} else {
					details.put(REQUIRED, "Height = 0.8");
					details.put(PROVIDED, "Height = " + minHeight);
					details.put(STATUS, Result.Not_Accepted.getResultVal());
					scrutinyDetail.getDetail().add(details);
					pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
				}
			}
		}
	}

	private void validateGenrailStairParapet(Plan pl) {
		for (Block b : pl.getBlocks()) {
			ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
			scrutinyDetail.setKey("Block_" + b.getNumber() + "_" + "General Stair Railling");
			scrutinyDetail.addColumnHeading(1, RULE_NO);
			scrutinyDetail.addColumnHeading(2, DESCRIPTION);
			scrutinyDetail.addColumnHeading(3, REQUIRED);
			scrutinyDetail.addColumnHeading(4, PROVIDED);
			scrutinyDetail.addColumnHeading(5, STATUS);
			Map<String, String> details = new HashMap<>();
			details.put(RULE_NO, RULE_41_V);
			details.put(DESCRIPTION, "General Stair Railling");

			BigDecimal minHeight = BigDecimal.ZERO;

			if (isGenralStairPersent(b)) {
				try {
					minHeight = b.getGenralStairParapets().stream().reduce(BigDecimal::min).get();
				} catch (Exception e) {
				}

				if (minHeight.compareTo(new BigDecimal("0.9")) >= 0) {
					details.put(REQUIRED, "Height >= 0.9");
					details.put(PROVIDED, "Height >= " + minHeight);
					details.put(STATUS, Result.Accepted.getResultVal());
					scrutinyDetail.getDetail().add(details);
					pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);

				} else {
					details.put(REQUIRED, "Height >= 0.9");
					details.put(PROVIDED, "Height >= " + minHeight);
					details.put(STATUS, Result.Not_Accepted.getResultVal());
					scrutinyDetail.getDetail().add(details);
					pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
				}
			}
		}

	}

	private boolean isGenralStairPersent(Block block) {

		for (Floor f : block.getBuilding().getFloors()) {
			if (f.getGeneralStairs() != null && f.getGeneralStairs().size() > 0)
				return true;
		}

		return false;
	}

	private void prepareParapet(Plan pl) {
		List<BigDecimal> genralStairParapets = new ArrayList<BigDecimal>();
		List<BigDecimal> dARailingParapets = new ArrayList<BigDecimal>();
		List<BigDecimal> genralParapets = new ArrayList<BigDecimal>();
		for (Block block : pl.getBlocks()) {
			for (Measurement measurement : block.getParapetWithColor()) {
				switch (measurement.getColorCode()) {
				case COLOR_GENRAL_STAIR_CASE_RAILLING:
					genralStairParapets.add(measurement.getHeight());
					break;
				case COLOR_DARAMP_RAILLING:
					dARailingParapets.add(measurement.getHeight());
					break;
				case COLOR_PARAPET:
					genralParapets.add(measurement.getHeight());
					break;
				}
			}

			block.setGenralStairParapets(genralStairParapets);
			block.setdARailingParapets(dARailingParapets);
			block.setGenralParapets(genralParapets);

		}

	}

	@Override
	public Map<String, Date> getAmendments() {
		return new LinkedHashMap<>();
	}

}
