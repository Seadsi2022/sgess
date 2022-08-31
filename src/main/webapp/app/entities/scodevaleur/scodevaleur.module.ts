import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ScodevaleurComponent } from './list/scodevaleur.component';
import { ScodevaleurDetailComponent } from './detail/scodevaleur-detail.component';
import { ScodevaleurUpdateComponent } from './update/scodevaleur-update.component';
import { ScodevaleurDeleteDialogComponent } from './delete/scodevaleur-delete-dialog.component';
import { ScodevaleurRoutingModule } from './route/scodevaleur-routing.module';

@NgModule({
  imports: [SharedModule, ScodevaleurRoutingModule],
  declarations: [ScodevaleurComponent, ScodevaleurDetailComponent, ScodevaleurUpdateComponent, ScodevaleurDeleteDialogComponent],
})
export class ScodevaleurModule {}
