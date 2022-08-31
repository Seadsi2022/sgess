import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IScodevaleur } from '../scodevaleur.model';
import { ScodevaleurService } from '../service/scodevaleur.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './scodevaleur-delete-dialog.component.html',
})
export class ScodevaleurDeleteDialogComponent {
  scodevaleur?: IScodevaleur;

  constructor(protected scodevaleurService: ScodevaleurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.scodevaleurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
