/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Common Scheduler Operations

    File        :   TaskDetail.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    TaskDetail.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-06  TSebo    First release version
*/
package oracle.iam.identity.scheduler.type;

import java.util.Date;


/**
 ** Hold information about OIM OpenTask
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 */
public class TaskDetail {
    private long processTaskKey;
    private String accountName;
    private String taskName;
    private String status;
    private byte[] version;
    private String beneficiary;
    private Date creationDate;
    private Date startDate;
    // optional
    private String note;
    private String reason;
    private String requestKey;
    private String requestJustification = "";
    private String requestFailureReason = "";
    private String wfTaskId;

    public TaskDetail(long processTaskKey) {
        super();
        this.processTaskKey = processTaskKey;
    }

    public TaskDetail(long processTaskKey, String accountName,
                      String taskName, String status, String beneficiary,
                      byte[] version, Date creationDate, Date startDate) {
        super();
        this.processTaskKey = processTaskKey;
        this.accountName    = accountName;
        this.taskName       = taskName;
        this.status         = status;
        this.beneficiary    = beneficiary;
        this.version        = version;
        this.creationDate   = creationDate;
        this.startDate      = startDate;
    }
    
    public TaskDetail(long processTaskKey, String accountName,
                      String taskName, String status, String beneficiary,
                      byte[] version, Date creationDate, Date startDate,
                      String note, String reason, String requestKey) {
        this(processTaskKey,accountName,taskName,status,beneficiary,version,creationDate,startDate );
        this.note           = note;
        this.reason         = reason;
        this.requestKey     = requestKey;
    }

    public long getProcessTaskKey() {
        return processTaskKey;
    }

    public void setProcessTaskKey(long processTaskKey) {
        this.processTaskKey = processTaskKey;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String objectName) {
        this.accountName = objectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public byte[] getVersion() {
        return version;
    }

    public void setVersion(byte[] version) {
        this.version = version;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("processTaskKey=").append(processTaskKey);
        sb.append(",resourceObjectName=").append(accountName);
        sb.append(",taskName=").append(taskName);
        sb.append(",status=").append(status);
        sb.append(",beneficiary=").append(beneficiary);
        sb.append(",creationDate=").append(creationDate);
        sb.append(",startDate=").append(startDate);
        sb.append(",reason=").append((reason == null ? "" : reason.replace("\n", "")));
        sb.append(",note=").append((note == null ? "" : note.replace("\n", "")));
        sb.append(",requestKey=").append(requestKey);
        sb.append(",requestJustification=").append(requestJustification);
        sb.append(",requestFailureReason=").append(requestFailureReason);
        sb.append(",wfTaskId=").append(wfTaskId);
        sb.append(",version=").append(version);
        return sb.toString();
    }

    public String toCsv() {
        StringBuffer sb = new StringBuffer();
        sb.append("\"");
        sb.append(processTaskKey);
        sb.append("\",\"").append(accountName    == null ? "" : accountName);
        sb.append("\",\"").append(taskName      == null ? "" : taskName);
        sb.append("\",\"").append(status        == null ? "" : status);
        sb.append("\",\"").append(beneficiary   == null ? "" : beneficiary);
        sb.append("\",\"").append(creationDate  == null ? "" : creationDate);
        sb.append("\",\"").append(startDate     == null ? "" : startDate);
        sb.append("\",\"").append(reason        == null ? "" : reason.replace("\n", ""));
        sb.append("\",\"").append(note          == null ? "" : note.replace("\n", ""));
        sb.append("\",\"").append(requestKey    == null ? "" : requestKey);
        sb.append("\",\"").append(requestJustification == null ? "" : requestJustification);
        sb.append("\",\"").append(requestFailureReason == null ? "" : requestFailureReason);
        sb.append("\"");
        return sb.toString();
    }

    public static String csvHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("processTaskKey");
        sb.append(",resourceObjectName");
        sb.append(",taskName");
        sb.append(",status");
        sb.append(",beneficiary");
        sb.append(",creationDate");
        sb.append(",startDate");
        sb.append(",reason");
        sb.append(",note");
        sb.append(",requestKey");
        sb.append(",requestJustification");
        sb.append(",requestFailureReason");
        return sb.toString();
    }


    public void setRequestJustification(String requestJustification) {
        this.requestJustification = requestJustification;
    }

    public String getRequestJustification() {
        return requestJustification;
    }

    public void setRequestFailureReason(String requestFailureReason) {
        this.requestFailureReason = requestFailureReason;
    }

    public String getRequestFailureReason() {
        return requestFailureReason;
    }

    public void setWfTaskId(String wfTaskId) {
        this.wfTaskId = wfTaskId;
    }

    public String getWfTaskId() {
        return wfTaskId;
    }
}
