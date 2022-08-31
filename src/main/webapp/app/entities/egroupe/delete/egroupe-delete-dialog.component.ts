import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgroupe } from '../egroupe.model';
import { EgroupeService } from '../service/egroupe.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './egroupe-delete-dialog.component.html',
})
export class EgroupeDeleteDialogComponent {
  egroupe?: IEgroupe;

  constructor(protected egroupeService: EgroupeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egroupeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
