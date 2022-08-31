import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEeventualite } from '../eeventualite.model';
import { EeventualiteService } from '../service/eeventualite.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './eeventualite-delete-dialog.component.html',
})
export class EeventualiteDeleteDialogComponent {
  eeventualite?: IEeventualite;

  constructor(protected eeventualiteService: EeventualiteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eeventualiteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
