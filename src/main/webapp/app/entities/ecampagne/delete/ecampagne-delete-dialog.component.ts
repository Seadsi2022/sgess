import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEcampagne } from '../ecampagne.model';
import { EcampagneService } from '../service/ecampagne.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ecampagne-delete-dialog.component.html',
})
export class EcampagneDeleteDialogComponent {
  ecampagne?: IEcampagne;

  constructor(protected ecampagneService: EcampagneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ecampagneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
