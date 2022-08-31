import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EgroupeFormService, EgroupeFormGroup } from './egroupe-form.service';
import { IEgroupe } from '../egroupe.model';
import { EgroupeService } from '../service/egroupe.service';
import { IEquestionnaire } from 'app/entities/equestionnaire/equestionnaire.model';
import { EquestionnaireService } from 'app/entities/equestionnaire/service/equestionnaire.service';

@Component({
  selector: 'jhi-egroupe-update',
  templateUrl: './egroupe-update.component.html',
})
export class EgroupeUpdateComponent implements OnInit {
  isSaving = false;
  egroupe: IEgroupe | null = null;

  equestionnairesSharedCollection: IEquestionnaire[] = [];

  editForm: EgroupeFormGroup = this.egroupeFormService.createEgroupeFormGroup();

  constructor(
    protected egroupeService: EgroupeService,
    protected egroupeFormService: EgroupeFormService,
    protected equestionnaireService: EquestionnaireService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEquestionnaire = (o1: IEquestionnaire | null, o2: IEquestionnaire | null): boolean =>
    this.equestionnaireService.compareEquestionnaire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egroupe }) => {
      this.egroupe = egroupe;
      if (egroupe) {
        this.updateForm(egroupe);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egroupe = this.egroupeFormService.getEgroupe(this.editForm);
    if (egroupe.id !== null) {
      this.subscribeToSaveResponse(this.egroupeService.update(egroupe));
    } else {
      this.subscribeToSaveResponse(this.egroupeService.create(egroupe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgroupe>>): void {
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

  protected updateForm(egroupe: IEgroupe): void {
    this.egroupe = egroupe;
    this.egroupeFormService.resetForm(this.editForm, egroupe);

    this.equestionnairesSharedCollection = this.equestionnaireService.addEquestionnaireToCollectionIfMissing<IEquestionnaire>(
      this.equestionnairesSharedCollection,
      egroupe.equestionnaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.equestionnaireService
      .query()
      .pipe(map((res: HttpResponse<IEquestionnaire[]>) => res.body ?? []))
      .pipe(
        map((equestionnaires: IEquestionnaire[]) =>
          this.equestionnaireService.addEquestionnaireToCollectionIfMissing<IEquestionnaire>(equestionnaires, this.egroupe?.equestionnaire)
        )
      )
      .subscribe((equestionnaires: IEquestionnaire[]) => (this.equestionnairesSharedCollection = equestionnaires));
  }
}
