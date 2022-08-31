import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SlocaliteFormService, SlocaliteFormGroup } from './slocalite-form.service';
import { ISlocalite } from '../slocalite.model';
import { SlocaliteService } from '../service/slocalite.service';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ScodevaleurService } from 'app/entities/scodevaleur/service/scodevaleur.service';

@Component({
  selector: 'jhi-slocalite-update',
  templateUrl: './slocalite-update.component.html',
})
export class SlocaliteUpdateComponent implements OnInit {
  isSaving = false;
  slocalite: ISlocalite | null = null;

  slocalitesSharedCollection: ISlocalite[] = [];
  scodevaleursSharedCollection: IScodevaleur[] = [];

  editForm: SlocaliteFormGroup = this.slocaliteFormService.createSlocaliteFormGroup();

  constructor(
    protected slocaliteService: SlocaliteService,
    protected slocaliteFormService: SlocaliteFormService,
    protected scodevaleurService: ScodevaleurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSlocalite = (o1: ISlocalite | null, o2: ISlocalite | null): boolean => this.slocaliteService.compareSlocalite(o1, o2);

  compareScodevaleur = (o1: IScodevaleur | null, o2: IScodevaleur | null): boolean => this.scodevaleurService.compareScodevaleur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ slocalite }) => {
      this.slocalite = slocalite;
      if (slocalite) {
        this.updateForm(slocalite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const slocalite = this.slocaliteFormService.getSlocalite(this.editForm);
    if (slocalite.id !== null) {
      this.subscribeToSaveResponse(this.slocaliteService.update(slocalite));
    } else {
      this.subscribeToSaveResponse(this.slocaliteService.create(slocalite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISlocalite>>): void {
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

  protected updateForm(slocalite: ISlocalite): void {
    this.slocalite = slocalite;
    this.slocaliteFormService.resetForm(this.editForm, slocalite);

    this.slocalitesSharedCollection = this.slocaliteService.addSlocaliteToCollectionIfMissing<ISlocalite>(
      this.slocalitesSharedCollection,
      slocalite.parent
    );
    this.scodevaleursSharedCollection = this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(
      this.scodevaleursSharedCollection,
      slocalite.natureLocalite,
      slocalite.typeLocalite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.slocaliteService
      .query()
      .pipe(map((res: HttpResponse<ISlocalite[]>) => res.body ?? []))
      .pipe(
        map((slocalites: ISlocalite[]) =>
          this.slocaliteService.addSlocaliteToCollectionIfMissing<ISlocalite>(slocalites, this.slocalite?.parent)
        )
      )
      .subscribe((slocalites: ISlocalite[]) => (this.slocalitesSharedCollection = slocalites));

    this.scodevaleurService
      .query()
      .pipe(map((res: HttpResponse<IScodevaleur[]>) => res.body ?? []))
      .pipe(
        map((scodevaleurs: IScodevaleur[]) =>
          this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(
            scodevaleurs,
            this.slocalite?.natureLocalite,
            this.slocalite?.typeLocalite
          )
        )
      )
      .subscribe((scodevaleurs: IScodevaleur[]) => (this.scodevaleursSharedCollection = scodevaleurs));
  }
}
