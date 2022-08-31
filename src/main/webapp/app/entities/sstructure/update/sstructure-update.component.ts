import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SstructureFormService, SstructureFormGroup } from './sstructure-form.service';
import { ISstructure } from '../sstructure.model';
import { SstructureService } from '../service/sstructure.service';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ScodevaleurService } from 'app/entities/scodevaleur/service/scodevaleur.service';
import { ISlocalite } from 'app/entities/slocalite/slocalite.model';
import { SlocaliteService } from 'app/entities/slocalite/service/slocalite.service';

@Component({
  selector: 'jhi-sstructure-update',
  templateUrl: './sstructure-update.component.html',
})
export class SstructureUpdateComponent implements OnInit {
  isSaving = false;
  sstructure: ISstructure | null = null;

  sstructuresSharedCollection: ISstructure[] = [];
  scodevaleursSharedCollection: IScodevaleur[] = [];
  slocalitesSharedCollection: ISlocalite[] = [];

  editForm: SstructureFormGroup = this.sstructureFormService.createSstructureFormGroup();

  constructor(
    protected sstructureService: SstructureService,
    protected sstructureFormService: SstructureFormService,
    protected scodevaleurService: ScodevaleurService,
    protected slocaliteService: SlocaliteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSstructure = (o1: ISstructure | null, o2: ISstructure | null): boolean => this.sstructureService.compareSstructure(o1, o2);

  compareScodevaleur = (o1: IScodevaleur | null, o2: IScodevaleur | null): boolean => this.scodevaleurService.compareScodevaleur(o1, o2);

  compareSlocalite = (o1: ISlocalite | null, o2: ISlocalite | null): boolean => this.slocaliteService.compareSlocalite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sstructure }) => {
      this.sstructure = sstructure;
      if (sstructure) {
        this.updateForm(sstructure);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sstructure = this.sstructureFormService.getSstructure(this.editForm);
    if (sstructure.id !== null) {
      this.subscribeToSaveResponse(this.sstructureService.update(sstructure));
    } else {
      this.subscribeToSaveResponse(this.sstructureService.create(sstructure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISstructure>>): void {
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

  protected updateForm(sstructure: ISstructure): void {
    this.sstructure = sstructure;
    this.sstructureFormService.resetForm(this.editForm, sstructure);

    this.sstructuresSharedCollection = this.sstructureService.addSstructureToCollectionIfMissing<ISstructure>(
      this.sstructuresSharedCollection,
      sstructure.parent
    );
    this.scodevaleursSharedCollection = this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(
      this.scodevaleursSharedCollection,
      ...(sstructure.scodes ?? [])
    );
    this.slocalitesSharedCollection = this.slocaliteService.addSlocaliteToCollectionIfMissing<ISlocalite>(
      this.slocalitesSharedCollection,
      sstructure.slocalite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sstructureService
      .query()
      .pipe(map((res: HttpResponse<ISstructure[]>) => res.body ?? []))
      .pipe(
        map((sstructures: ISstructure[]) =>
          this.sstructureService.addSstructureToCollectionIfMissing<ISstructure>(sstructures, this.sstructure?.parent)
        )
      )
      .subscribe((sstructures: ISstructure[]) => (this.sstructuresSharedCollection = sstructures));

    this.scodevaleurService
      .query()
      .pipe(map((res: HttpResponse<IScodevaleur[]>) => res.body ?? []))
      .pipe(
        map((scodevaleurs: IScodevaleur[]) =>
          this.scodevaleurService.addScodevaleurToCollectionIfMissing<IScodevaleur>(scodevaleurs, ...(this.sstructure?.scodes ?? []))
        )
      )
      .subscribe((scodevaleurs: IScodevaleur[]) => (this.scodevaleursSharedCollection = scodevaleurs));

    this.slocaliteService
      .query()
      .pipe(map((res: HttpResponse<ISlocalite[]>) => res.body ?? []))
      .pipe(
        map((slocalites: ISlocalite[]) =>
          this.slocaliteService.addSlocaliteToCollectionIfMissing<ISlocalite>(slocalites, this.sstructure?.slocalite)
        )
      )
      .subscribe((slocalites: ISlocalite[]) => (this.slocalitesSharedCollection = slocalites));
  }
}
