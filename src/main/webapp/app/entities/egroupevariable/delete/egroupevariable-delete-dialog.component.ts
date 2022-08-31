import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgroupevariable } from '../egroupevariable.model';
import { EgroupevariableService } from '../service/egroupevariable.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './egroupevariable-delete-dialog.component.html',
})
export class EgroupevariableDeleteDialogComponent {
  egroupevariable?: IEgroupevariable;

  constructor(protected egroupevariableService: EgroupevariableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egroupevariableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
