package com.epam.esm.util;

import com.epam.esm.audit.AuditType;
import com.epam.esm.entity.Identifiable;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;

public class AuditUtils {

    public static void audit(AuditType auditType, Identifiable entity) {
        Logger logger = Logger.getLogger(entity.getClass());
        logger.debug(LocalDateTime.now() + "  " + auditType + " " + entity);
    }
}
