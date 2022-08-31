import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISetablissement } from '../setablissement.model';
import { SetablissementService } from '../service/setablissement.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './setablissement-delete-dialog.component.html',
})
export class SetablissementDeleteDialogComponent {
  setablissement?: ISetablissement;

  constructor(protected setablissementService: SetablissementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.setablissementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
