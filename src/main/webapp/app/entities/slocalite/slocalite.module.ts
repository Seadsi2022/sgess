import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SlocaliteComponent } from './list/slocalite.component';
import { SlocaliteDetailComponent } from './detail/slocalite-detail.component';
import { SlocaliteUpdateComponent } from './update/slocalite-update.component';
import { SlocaliteDeleteDialogComponent } from './delete/slocalite-delete-dialog.component';
import { SlocaliteRoutingModule } from './route/slocalite-routing.module';

@NgModule({
  imports: [SharedModule, SlocaliteRoutingModule],
  declarations: [SlocaliteComponent, SlocaliteDetailComponent, SlocaliteUpdateComponent, SlocaliteDeleteDialogComponent],
})
export class SlocaliteModule {}
