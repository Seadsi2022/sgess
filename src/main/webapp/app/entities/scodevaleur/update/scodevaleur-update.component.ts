import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ScodevaleurFormService, ScodevaleurFormGroup } from './scodevaleur-form.service';
import { IScodevaleur } from '../scodevaleur.model';
import { ScodevaleurService } from '../service/scodevaleur.service';
import { IScode } from 'app/entities/scode/scode.model';
import { ScodeService } from 'app/entities/scode/service/scode.service';

@Component({
  selector: 'jhi-scodevaleur-update',
  templateUrl: './scodevaleur-update.component.html',
})
export class ScodevaleurUpdateComponent implements OnInit {
  isSaving = false;
  scodevaleur: IScodevaleur | null = null;

  scodevaleursSharedCollection: IScodevaleur[] = [];
  scodesSharedCollection: IScode[] = [];

  editForm: ScodevaleurFormGroup = this.scodevaleurFormService.createScodevaleurFormGroup();

  constructor(
    protected scodevaleurService: ScodevaleurService,
    protected scodevaleurFormService: ScodevaleurFormService,
    protected scodeService: ScodeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareScodevaleur = (o1: IScodevaleur | null, o2: IScodevaleur | null): boolean => this.scodevaleurService.compareScodevaleur(o1, o2);

  compareScode = (o1: IScode | null, o2: IScode | null): boolean => this.scodeService.compareScode(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ scodevaleur }) => {
      this.scodevaleur = scodevaleur;
      if (scodevaleur) {
        this.updateForm(scodevaleur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const scodevaleur = this.scodevaleurFormService.getScodevaleur(this.editForm);
    if (scodevaleur.id !== null) {
      this.subscribeToSaveResponse(this.scodevaleurService.update(scodevaleur));
    } else {
      this.subscribeToSaveResponse(this.scodevaleurService.create(scodevaleur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScodevaleur>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(scodevaleur: IScodevaleur): void {
    this.scodevaleur = scodevaleur;
    this.scodevaleurFormService.resetForm(this.editForm, scodevaleur);

    this.scodevaleursSharedCollection = this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(
      this.scodevaleursSharedCollection,
      scodevaleur.parent
    );
    this.scodesSharedCollection = this.scodeService.addScodeToCollectionIfMissing<IScode>(this.scodesSharedCollection, scodevaleur.scode);
  }

  protected loadRelationshipsOptions(): void {
    this.scodevaleurService
      .query()
      .pipe(map((res: HttpResponse<IScodevaleur[]>) => res.body ?? []))
      .pipe(
        map((scodevaleurs: IScodevaleur[]) =>
          this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(scodevaleurs, this.scodevaleur?.parent)
        )
      )
      .subscribe((scodevaleurs: IScodevaleur[]) => (this.scodevaleursSharedCollection = scodevaleurs));

    this.scodeService
      .query()
      .pipe(map((res: HttpResponse<IScode[]>) => res.body ?? []))
      .pipe(map((scodes: IScode[]) => this.scodeService.addScodeToCollectionIfMissing<IScode>(scodes, this.scodevaleur?.scode)))
      .subscribe((scodes: IScode[]) => (this.scodesSharedCollection = scodes));
  }
}
