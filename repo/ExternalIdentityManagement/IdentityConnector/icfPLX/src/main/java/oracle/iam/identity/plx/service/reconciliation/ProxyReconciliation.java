package oracle.iam.identity.plx.service.reconciliation;

import oracle.iam.identity.connector.service.DescriptorTransformer;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.resource.TaskBundle;

import org.identityconnectors.framework.common.objects.OperationOptions;

public class ProxyReconciliation extends LookupReconciliation {

  public ProxyReconciliation() {
    super();
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Directory Service.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));
    // obtain the state of the job before any of the attributes are subject of
    // change
    final OperationOptions options = operationOptions();
    // set the current date as the timestamp on which this task has been last
    // reconciled at the start of execution
    // setting it here to have it the next time this scheduled task will
    // run the changes made during the execution of this task
    // updating this attribute will not perform to write it back to the
    // scheduled job attributes it's still in memory; updateLastReconciled()
    // will persist the change that we do here only if the job completes
    // successful
    lastReconciled(systemTime());
    try {
      this.connector.search(DescriptorTransformer.objectClass(reconcileSource()), filter(), this, options);
    }
    finally {
      // inform the observing user about the overall result of this task
      if (isStopped()) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), "Veto"};
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // if an exception occured
      else if (getResult() != null) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), getResult().getLocalizedMessage()};
        error("onExecution", TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // complete with success and write back timestamp
      else {
        // update the timestamp on the task
        updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
      }
    }
  }
}
